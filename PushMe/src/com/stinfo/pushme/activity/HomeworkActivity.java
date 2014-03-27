package com.stinfo.pushme.activity;

import java.util.ArrayList;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.stinfo.pushme.R;
import com.stinfo.pushme.BaseActionBarActivity;
import com.stinfo.pushme.adapter.HomeworkListAdapter;
import com.stinfo.pushme.common.AppConstant;
import com.stinfo.pushme.common.UserCache;
import com.stinfo.pushme.db.DBAdapter;
import com.stinfo.pushme.entity.ClassInfo;
import com.stinfo.pushme.entity.Homework;
import com.stinfo.pushme.entity.Teacher;
import com.stinfo.pushme.rest.MyStringRequest;
import com.stinfo.pushme.rest.RequestController;
import com.stinfo.pushme.rest.entity.QueryHomeworkReq;
import com.stinfo.pushme.rest.entity.QueryHomeworkResp;
import com.stinfo.pushme.util.MessageBox;
import com.stinfo.pushme.view.DropDownListView;
import com.stinfo.pushme.view.DropDownListView.OnDropDownListener;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.OnNavigationListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;

public class HomeworkActivity extends BaseActionBarActivity implements OnNavigationListener,
		OnItemClickListener {
	private static final String TAG = "HomeworkActivity";
	private ArrayList<Homework> mHomeworkList = new ArrayList<Homework>();
	private HomeworkListAdapter mAdapter;
	private String[] mItems;
	private DropDownListView lvHomeworkList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_homework);

		ActionBar bar = getSupportActionBar();
		bar.setTitle(getResources().getString(R.string.homework));
		bar.setDisplayHomeAsUpEnabled(true);
		initView();
		initData();
	}

	private void initView() {
		ActionBar bar = getSupportActionBar();
		bar.setDisplayHomeAsUpEnabled(true);

		mItems = getResources().getStringArray(R.array.subject_list);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
				mItems);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		bar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		bar.setListNavigationCallbacks(adapter, this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.notice, menu);
		if (!(UserCache.getInstance().getUserInfo() instanceof Teacher)) {
			MenuItem editHomework = menu.findItem(R.id.action_edit);
			editHomework.setVisible(false);
		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_edit:
			onEditHomework();
			break;
		case android.R.id.home:
			this.finish();
			break;
		}
		return true;
	}
	
	@Override
	public void onPause() {
		RequestController.getInstance().cancelPendingRequests(TAG);
		super.onPause();
	}

	public void onEditHomework() {
		Intent intent = new Intent(this, PostHomeworkActivity.class);
		startActivity(intent);
	}

	@Override
	public boolean onNavigationItemSelected(int arg0, long arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	private void initData() {
		mHomeworkList.clear();
		lvHomeworkList = (DropDownListView) findViewById(R.id.lv_homework);
		mAdapter = new HomeworkListAdapter(this, mHomeworkList);
		lvHomeworkList.setAdapter(mAdapter);

		lvHomeworkList.setOnItemClickListener(this);
		lvHomeworkList.setOnDropDownListener(new OnDropDownListener() {
			@Override
			public void onDropDown() {
				refreshHomeworkList();
			}
		});

		initHomeworkList();
	}

	private void initHomeworkList() {
		DBAdapter dbAdapter = new DBAdapter();
		ClassInfo classInfo = UserCache.getInstance().getCurrentClass();
		try {
			dbAdapter.open();
			mHomeworkList.addAll(dbAdapter.getClassHomework(classInfo.getClassId()));
			mAdapter.notifyDataSetChanged();
			lvHomeworkList.setSecondPositionVisible();
			lvHomeworkList.onDropDownComplete();
		} catch (Exception e) {
			Log.e(TAG, "Failed to operate database: " + e);
		} finally {
			dbAdapter.close();
		}
	}

	public void refreshHomeworkList() {
		UserCache userCache = UserCache.getInstance();
		QueryHomeworkReq reqData = new QueryHomeworkReq();
		String createTime = "0";

		reqData.setUserId(userCache.getUserInfo().getUserId());
		reqData.setSessionKey(userCache.getSessionKey());
		reqData.setQueryType(AppConstant.HomeworkQueryType.QUERY_CLASS);
		reqData.setQueryValue(userCache.getCurrentClass().getClassId());
		reqData.setSubject("");
		if (mHomeworkList.size() > 0) {
			createTime = mHomeworkList.get(0).getCreateTime();
		}
		reqData.setLastCreateTime(createTime);

		MyStringRequest req = new MyStringRequest(Method.GET, reqData.getAllUrl(),
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						Log.v(TAG, "Response: " + response);
						checkResponse(response);
						lvHomeworkList.onDropDownComplete();
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						lvHomeworkList.onDropDownComplete();
						MessageBox.showServerError(HomeworkActivity.this);
					}
				});

		RequestController.getInstance().addToRequestQueue(req, TAG);
	}

	private void checkResponse(String response) {
		try {
			QueryHomeworkResp respData = new QueryHomeworkResp(response);
			if (respData.getAck() == AppConstant.Ack.SUCCESS) {
				doSuccess(respData);
			} else if (respData.getAck() != AppConstant.Ack.NOT_FOUND) {
				MessageBox.showAckError(HomeworkActivity.this, respData.getAck());
			}
		} catch (Exception e) {
			MessageBox.showParserError(HomeworkActivity.this);
			Log.e(TAG, "Failed to parser response data! \r\n" + e);
		}
	}

	private void doSuccess(QueryHomeworkResp respData) {
		mHomeworkList.addAll(0, respData.getHomeworkList());
		mAdapter.notifyDataSetChanged();
		DBAdapter dbAdapter = new DBAdapter();
		try {
			dbAdapter.open();
			dbAdapter.addHomework(respData.getHomeworkList());
		} catch (Exception e) {
			Log.e(TAG, "Failed to operate database: " + e);
		} finally {
			dbAdapter.close();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View v, int pos, long id) {
		switch (parent.getId()) {
		case R.id.lv_homework:
			break;
		}
	}
}

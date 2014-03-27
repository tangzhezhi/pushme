package com.stinfo.pushme.fragment;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.stinfo.pushme.R;
import com.stinfo.pushme.activity.NoticeDetailActivity;
import com.stinfo.pushme.adapter.NoticeListAdapter;
import com.stinfo.pushme.common.AppConstant;
import com.stinfo.pushme.common.UserCache;
import com.stinfo.pushme.db.DBAdapter;
import com.stinfo.pushme.entity.ClassInfo;
import com.stinfo.pushme.entity.Notice;
import com.stinfo.pushme.rest.MyStringRequest;
import com.stinfo.pushme.rest.RequestController;
import com.stinfo.pushme.rest.entity.QueryNoticeReq;
import com.stinfo.pushme.rest.entity.QueryNoticeResp;
import com.stinfo.pushme.util.MessageBox;
import com.stinfo.pushme.view.DropDownListView;
import com.stinfo.pushme.view.DropDownListView.OnDropDownListener;

public final class NoticeSchoolFragment extends Fragment implements OnItemClickListener {
	private static final String TAG = "NoticeSchoolFragment";
	private ArrayList<Notice> mNoticeList = new ArrayList<Notice>();
	private NoticeListAdapter mAdapter;
	private DropDownListView lvNoticeList;
	private View mView;
	
	public static NoticeSchoolFragment newInstance() {
		NoticeSchoolFragment newFragment = new NoticeSchoolFragment();
		return newFragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.fragment_notice_list, container, false);
		return mView;
	}

	@Override
	public void onResume() {
		super.onResume();
		initData();
	}

	@Override
	public void onPause() {
		super.onPause();
		RequestController.getInstance().cancelPendingRequests(TAG);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	private void initData() {
		mNoticeList.clear();
		lvNoticeList = (DropDownListView) mView.findViewById(R.id.lv_notice_list);
		mAdapter = new NoticeListAdapter(mView.getContext(), mNoticeList);
		lvNoticeList.setAdapter(mAdapter);
		
		lvNoticeList.setOnItemClickListener(this);
		lvNoticeList.setOnDropDownListener(new OnDropDownListener() {
			@Override
			public void onDropDown() {
				refreshNoticeList();
			}});
		
		initNoticeList();
	}

	private void initNoticeList() {
		DBAdapter dbAdapter = new DBAdapter();
		ClassInfo classInfo = UserCache.getInstance().getCurrentClass();
		try {
			dbAdapter.open();
			mNoticeList.addAll(dbAdapter.getNotice(classInfo.getSchoolId(), AppConstant.NoticeType.SCHOOL));
			mAdapter.notifyDataSetChanged();
			lvNoticeList.setSecondPositionVisible();
			lvNoticeList.onDropDownComplete();
		} catch (Exception e) {
			Log.e(TAG, "Failed to operate database: " + e);
		} finally {
			dbAdapter.close();
		}
	}

	public void refreshNoticeList() {
		UserCache userCache = UserCache.getInstance();
		QueryNoticeReq reqData = new QueryNoticeReq();
		String createTime = "0";
		
		reqData.setUserId(userCache.getUserInfo().getUserId());
		reqData.setSessionKey(userCache.getSessionKey());
		reqData.setQueryType(AppConstant.NoticeQueryType.QUERY_SCHOOL);
		reqData.setQueryValue(userCache.getCurrentClass().getClassId());
		if (mNoticeList.size() > 0) {
			createTime = mNoticeList.get(0).getCreateTime();
		}
		reqData.setLastCreateTime(createTime);

		MyStringRequest req = new MyStringRequest(Method.GET, reqData.getAllUrl(),
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						Log.v(TAG, "Response: " + response);
						checkResponse(response);
						lvNoticeList.onDropDownComplete();
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						lvNoticeList.onDropDownComplete();
						MessageBox.showServerError(mView.getContext());
					}
				});

		RequestController.getInstance().addToRequestQueue(req, TAG);
	}

	private void checkResponse(String response) {
		try {
			QueryNoticeResp respData = new QueryNoticeResp(response);
			if (respData.getAck() == AppConstant.Ack.SUCCESS) {
				doSuccess(respData);
			} else if (respData.getAck() != AppConstant.Ack.NOT_FOUND) {
				MessageBox.showAckError(mView.getContext(), respData.getAck());
			}
		} catch (Exception e) {
			MessageBox.showParserError(mView.getContext());
			Log.e(TAG, "Failed to parser response data! \r\n" + e);
		}
	}
	
	private void doSuccess(QueryNoticeResp respData) {
		mNoticeList.addAll(0, respData.getNoticeList());
		mAdapter.notifyDataSetChanged();
		DBAdapter dbAdapter = new DBAdapter();
		try {
			dbAdapter.open();
			dbAdapter.addNotice(respData.getNoticeList());
		} catch (Exception e) {
			Log.e(TAG, "Failed to operate database: " + e);
		} finally {
			dbAdapter.close();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View v, int pos, long id) {
		switch (parent.getId()) {
		case R.id.lv_notice_list:
			Intent intent = new Intent(getActivity(), NoticeDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("noticeList", mNoticeList);
            bundle.putInt("index", pos);
            intent.putExtras(bundle);
            getActivity().startActivity(intent);
            break;
		}
	}
}

package com.stinfo.pushme.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.stinfo.pushme.BaseActionBarActivity;
import com.stinfo.pushme.R;
import com.stinfo.pushme.common.AppConstant;
import com.stinfo.pushme.common.UserCache;
import com.stinfo.pushme.entity.Teacher;
import com.stinfo.pushme.rest.MyStringRequest;
import com.stinfo.pushme.rest.RequestController;
import com.stinfo.pushme.rest.entity.BaseResponse;
import com.stinfo.pushme.rest.entity.PostNoticeReq;
import com.stinfo.pushme.util.MessageBox;

public class PostNoticeActivity extends BaseActionBarActivity {
	private static final String TAG = "PostNoticeActivity";

	private ProgressDialog prgDialog = null;
	private TextView tvClassName;
	private EditText etTitle;
	private EditText etContent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_post_notice);
		ActionBar bar = getSupportActionBar();
		bar.setTitle(getResources().getString(R.string.post_notice));
		bar.setDisplayHomeAsUpEnabled(true);

		initView();
		initClassName();
	}

	private void initClassName() {
		UserCache userCache  = UserCache.getInstance();
		if (userCache.getUserInfo() instanceof Teacher) {
			tvClassName = (TextView) findViewById(R.id.tv_class_name);
			tvClassName.setText(userCache.getCurrentClass().getClassName());
		}
	}

	private void initView() {
		etTitle = (EditText) findViewById(R.id.et_notice_title);
		etContent = (EditText) findViewById(R.id.et_notice_content);
	}

	private void showProgressDialog() {
		prgDialog = new ProgressDialog(this);
		prgDialog.setMessage("正在提交...");
		prgDialog.show();
	}

	private void closeProgressDialog() {
		if ((prgDialog != null) && (prgDialog.isShowing())) {
			prgDialog.dismiss();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.post_notice, menu);
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_submit:
			onSubmit();
			break;
		case android.R.id.home:
			this.finish();
			break;
		}
		return true;
	}

	@Override
	protected void onPause() {
		super.onPause();
		RequestController.getInstance().cancelPendingRequests(TAG);
		closeProgressDialog();
	}

	private void onSubmit() {
		UserCache userCache  = UserCache.getInstance();
		if (!(userCache.getUserInfo() instanceof Teacher)) {
			MessageBox.showLongMessage(PostNoticeActivity.this, "仅允许教师用户发布通知公告！");
			return;
		}

		Teacher teacher = (Teacher) userCache.getUserInfo();
		PostNoticeReq reqData = new PostNoticeReq();
		reqData.setObjectId(userCache.getCurrentClass().getClassId());
		reqData.setUserId(teacher.getUserId());
		reqData.setSessionKey(userCache.getSessionKey());
		reqData.setType(AppConstant.NoticeType.CLASS);
		reqData.setTitle(etTitle.getText().toString());
		reqData.setContent(etContent.getText().toString());

		showProgressDialog();
		MyStringRequest req = new MyStringRequest(Method.POST, reqData.getAllUrl(),
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						Log.v(TAG, "Response: " + response);
						closeProgressDialog();
						checkResponse(response);
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						closeProgressDialog();
						MessageBox.showServerError(PostNoticeActivity.this);
					}
				});

		RequestController.getInstance().addToRequestQueue(req, TAG);
	}

	private void checkResponse(String response) {
		try {
			BaseResponse respData = new BaseResponse(response);
			if (respData.getAck() == AppConstant.Ack.SUCCESS) {
				MessageBox.showMessage(PostNoticeActivity.this, "提交成功！");
			} else {
				MessageBox.showAckError(PostNoticeActivity.this, respData.getAck());
			}
		} catch (Exception e) {
			MessageBox.showParserError(PostNoticeActivity.this);
			Log.e(TAG, "Failed to parser response data! \r\n" + e);
		}
	}
}

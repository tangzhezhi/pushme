package com.stinfo.pushme.activity;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.stinfo.pushme.R;
import com.stinfo.pushme.BaseActionBarActivity;
import com.stinfo.pushme.common.AppConstant;
import com.stinfo.pushme.common.SyncRoster;
import com.stinfo.pushme.common.UserCache;
import com.stinfo.pushme.entity.ClassInfo;
import com.stinfo.pushme.entity.Parent;
import com.stinfo.pushme.entity.Student;
import com.stinfo.pushme.entity.Teacher;
import com.stinfo.pushme.entity.UserInfo;
import com.stinfo.pushme.rest.MyStringRequest;
import com.stinfo.pushme.rest.RequestController;
import com.stinfo.pushme.rest.entity.UserLoginReq;
import com.stinfo.pushme.rest.entity.UserLoginResp;
import com.stinfo.pushme.util.MessageBox;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Paint;

public class LoginActivity extends BaseActionBarActivity implements OnClickListener {
	private static final String TAG = "LoginActivity";
	private ProgressDialog prgDialog = null;
	private TextView tvForgotPwd;
	private EditText etUserId;
	private EditText etPassword;
	private Button btnLogin;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		ActionBar bar = getSupportActionBar();
		bar.setTitle(getResources().getString(R.string.login));
		initView();
	}

	private void showProgressDialog() {
		prgDialog = new ProgressDialog(this);
		prgDialog.setMessage("正在登录...");
		prgDialog.show();
	}

	private void closeProgressDialog() {
		if ((prgDialog != null) && (prgDialog.isShowing())) {
			prgDialog.dismiss();
		}
	}

	private void initView() {
		tvForgotPwd = (TextView) findViewById(R.id.tv_forgot_password);
		tvForgotPwd.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);

		etUserId = (EditText) findViewById(R.id.et_user_id);
		etPassword = (EditText) findViewById(R.id.et_password);
		UserCache userCache = UserCache.getInstance();
		if (userCache.getUserInfo() != null) {
			etUserId.setText(userCache.getUserInfo().getUserId());
		}
		
		btnLogin = (Button) findViewById(R.id.btn_login);
		btnLogin.setOnClickListener(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		RequestController.getInstance().cancelPendingRequests(TAG);
		closeProgressDialog();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_login:
			onLogin(v);
			break;
		}
	}

	private void onLogin(View v) {
		UserLoginReq reqData = new UserLoginReq();
		reqData.setUserId(etUserId.getText().toString());
		reqData.setPassword(etPassword.getText().toString());

		showProgressDialog();
		UserCache.getInstance().setLogon(false);
		MyStringRequest req = new MyStringRequest(Method.POST, reqData.getAllUrl(),
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						Log.v(TAG, "Response: " + response);
						checkResponse(response);
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						closeProgressDialog();
						MessageBox.showServerError(LoginActivity.this);
					}
				});

		RequestController.getInstance().addToRequestQueue(req, TAG);
	}

	private void checkResponse(String response) {
		try {
			UserLoginResp respData = new UserLoginResp(response);
			if (respData.getAck() == AppConstant.Ack.SUCCESS) {
				loginSuccess(respData);
			} else {
				closeProgressDialog();
				MessageBox.showAckError(LoginActivity.this, respData.getAck());
			}
		} catch (Exception e) {
			closeProgressDialog();
			MessageBox.showParserError(LoginActivity.this);
			Log.e(TAG, "Failed to parser response data! \r\n" + e);
		}
	}

	private void loginSuccess(UserLoginResp respData) {
		UserInfo userInfo = respData.getUserInfo();
		UserCache userCache = UserCache.getInstance();

		userCache.setLogon(true);
		userCache.setSessionKey(respData.getSessionKey());
		// TBD: Encrypt password with MD5 or DES
		userCache.setPassword(etPassword.getText().toString());
		userCache.setUserInfo(userInfo);

		saveDefaultChild(userCache, userInfo);
		saveDefaultClass(userCache, userInfo);
		
		SyncRoster syncRoster = new SyncRoster(new SyncRoster.OnTaskListener() {

			@Override
			public void onSuccess() {
				closeProgressDialog();
				Intent intent = new Intent(LoginActivity.this, MainActivity.class);
				LoginActivity.this.startActivity(intent);
				LoginActivity.this.finish();
			}

			@Override
			public void onFailure() {
				closeProgressDialog();
				MessageBox.showLongMessage(LoginActivity.this, "同步通讯录失败!");
			}
		});
		
		syncRoster.execSync();
	}

	private void saveDefaultChild(UserCache userCache, UserInfo userInfo) {
		if (userInfo instanceof Parent) {
			Parent parent = (Parent) userInfo;
			if (parent.getChildList().size() > 0) {
				Student child = parent.getChildList().get(0);
				userCache.setCurrentChild(child);
			}
		}
	}

	private void saveDefaultClass(UserCache userCache, UserInfo userInfo) {
		ClassInfo defaultClass = getDefaultClass(userInfo);
		userCache.setCurrentClass(defaultClass);
	}

	private ClassInfo getDefaultClass(UserInfo userInfo) {
		ClassInfo defaultClass = new ClassInfo();

		if (userInfo instanceof Student) {
			Student student = (Student) userInfo;
			defaultClass.setSchoolId(student.getSchoolId());
			defaultClass.setClassId(student.getClassId());
			defaultClass.setClassName(student.getClassName());

		} else if (userInfo instanceof Parent) {
			Parent parent = (Parent) userInfo;
			if (parent.getChildList().size() > 0) {
				defaultClass.setSchoolId(parent.getChildList().get(0).getSchoolId());
				defaultClass.setClassId(parent.getChildList().get(0).getClassId());
				defaultClass.setClassName(parent.getChildList().get(0).getClassName());
			}

		} else if (userInfo instanceof Teacher) {
			Teacher teacher = (Teacher) userInfo;
			defaultClass.setSchoolId(teacher.getSchoolId());
			if (teacher.getTeacherRoleList().size() > 0) {
				defaultClass.setClassId(teacher.getTeacherRoleList().get(0).getClassId());
				defaultClass.setClassName(teacher.getTeacherRoleList().get(0).getClassName());
			}
		}

		return defaultClass;
	}
}
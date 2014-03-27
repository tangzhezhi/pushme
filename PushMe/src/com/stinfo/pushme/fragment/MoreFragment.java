package com.stinfo.pushme.fragment;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;

import com.stinfo.pushme.MyApplication;
import com.stinfo.pushme.R;
import com.stinfo.pushme.activity.AboutActivity;
import com.stinfo.pushme.activity.FeedbackActivity;
import com.stinfo.pushme.activity.LoginActivity;
import com.stinfo.pushme.activity.MyAccountActivity;
import com.stinfo.pushme.activity.SettingActivity;
import com.stinfo.pushme.common.AppConstant;
import com.stinfo.pushme.common.UserCache;
import com.stinfo.pushme.rest.MyStringRequest;
import com.stinfo.pushme.rest.RequestController;
import com.stinfo.pushme.rest.entity.LastAppVerReq;
import com.stinfo.pushme.rest.entity.LastAppVerResp;
import com.stinfo.pushme.util.MessageBox;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

public class MoreFragment extends Fragment implements OnClickListener {
	private final static String TAG = "MoreFragment";
	private RelativeLayout layoutMyAccount;
	private RelativeLayout layoutChangeClass;
	private RelativeLayout layoutClearCache;
	private RelativeLayout layoutFeedback;
	private RelativeLayout layoutSetting;
	private RelativeLayout layoutCheckUpdate;
	private RelativeLayout layoutAbout;
	private RelativeLayout layoutChangeAccout;
	private String mAppUrl = "";
	private View mView;

	public static MoreFragment newInstance() {
		MoreFragment newFragment = new MoreFragment();
		return newFragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.fragment_more, container, false);
		initView();

		return mView;
	}
	
	@Override
	public void onPause() {
		super.onPause();
		RequestController.getInstance().cancelPendingRequests(TAG);
	}

	private void initView() {
		layoutMyAccount = (RelativeLayout) mView.findViewById(R.id.layout_my_account);
		layoutChangeClass = (RelativeLayout) mView.findViewById(R.id.layout_change_class);
		layoutClearCache = (RelativeLayout) mView.findViewById(R.id.layout_clear_cache);
		layoutFeedback = (RelativeLayout) mView.findViewById(R.id.layout_feedback);
		layoutSetting = (RelativeLayout) mView.findViewById(R.id.layout_setting);
		layoutCheckUpdate = (RelativeLayout) mView.findViewById(R.id.layout_check_update);
		layoutAbout = (RelativeLayout) mView.findViewById(R.id.layout_about);
		layoutChangeAccout = (RelativeLayout) mView.findViewById(R.id.layout_change_account);

		layoutMyAccount.setOnClickListener(this);
		layoutChangeClass.setOnClickListener(this);
		layoutClearCache.setOnClickListener(this);
		layoutFeedback.setOnClickListener(this);
		layoutSetting.setOnClickListener(this);
		layoutCheckUpdate.setOnClickListener(this);
		layoutAbout.setOnClickListener(this);
		layoutChangeAccout.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_my_account:
			onMyAccount();
			break;
		case R.id.layout_change_class:
			onChangeClass();
			break;
		case R.id.layout_clear_cache:
			onClearCache();
			break;
		case R.id.layout_feedback:
			onFeedback();
			break;
		case R.id.layout_setting:
			onSetting();
			break;
		case R.id.layout_check_update:
			onCheckUpdate();
			break;
		case R.id.layout_about:
			onAbout();
			break;
		case R.id.layout_change_account:
			onChangeAccount();
			break;
		}
	}
	
	private void onMyAccount() {
		Intent intent = new Intent(getActivity(), MyAccountActivity.class);
		getActivity().startActivity(intent);
	}
	
	private void onChangeClass() {
	}
	
	private void onClearCache() {
	}
	
	private void onFeedback() {
		Intent intent = new Intent(getActivity(), FeedbackActivity.class);
		getActivity().startActivity(intent);
	}
	
	private void onSetting() {
		Intent intent = new Intent(getActivity(), SettingActivity.class);
		getActivity().startActivity(intent);
	}
	
	private void onAbout() {
		Intent intent = new Intent(getActivity(), AboutActivity.class);
		getActivity().startActivity(intent);
	}

	private void onChangeAccount() {
		UserCache.getInstance().setLogon(false);
		Intent intent = new Intent(getActivity(), LoginActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		getActivity().startActivity(intent);
		getActivity().finish();
	}
	
	private void onCheckUpdate() {
		LastAppVerReq reqData = new LastAppVerReq();
		reqData.setAppName("1");
		reqData.setAppType(1);

		MyStringRequest req = new MyStringRequest(Method.GET, reqData.getAllUrl(),
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						Log.v(TAG, "Response: " + response);
						checkResponse(response);
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						MessageBox.showServerError(getActivity());
					}
				});

		MessageBox.showMessage(getActivity(), "检查中..., 请稍等");
		RequestController.getInstance().addToRequestQueue(req, TAG);
	}
	
	private void checkResponse(String response) {
		try {
			LastAppVerResp respData = new LastAppVerResp(response);
			if (respData.getAck() == AppConstant.Ack.SUCCESS) {
				checkVersion(respData);
			} else {
				MessageBox.showAckError(getActivity(), respData.getAck());
			}
		} catch (Exception e) {
			MessageBox.showParserError(getActivity());
			Log.e(TAG, "Failed to parser response data! \r\n" + e);
		}
	}
	
	private void checkVersion(LastAppVerResp respData) {
		int localVersion = 0;
		try {
			localVersion = MyApplication.getInstance().getAppVersion();
		} catch (Exception ex) {
			Log.e(TAG, "Failed to get local package version! ", ex);
			return;
		}

		mAppUrl = respData.getAppUrl();
		Log.d(TAG, "localVersion: " + localVersion);
		Log.d(TAG, "appUrl: " + mAppUrl);
		Log.d(TAG, "serverVersion: " + respData.getVersionCode());
		Log.d(TAG, "serverVersionName: " + respData.getVersionName());
		Log.d(TAG, "updateTime: " + respData.getUpdateTime());
		String promptMsg = "发现新版本 V" + respData.getVersionName() + ", 建议立即更新使用";

		if (localVersion < respData.getVersionCode()) {
			new AlertDialog.Builder(getActivity()).setTitle("软件升级").setMessage(promptMsg)
					.setPositiveButton("更新", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							updatePackage();
						}
					}).setNegativeButton("取消", null).show();
		} else {
			MessageBox.showMessage(getActivity(), "已是最新版本, 无需升级");
		}
	}

	private void updatePackage() {
		Intent updateIntent = new Intent("com.stinfo.pushme.UPDATE_SERVICE");
		updateIntent.putExtra("appName", getResources().getString(R.string.app_name));
		updateIntent.putExtra("appUrl", mAppUrl);
		getActivity().startService(updateIntent);
	}
}

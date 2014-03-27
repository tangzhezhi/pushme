package com.stinfo.pushme.activity;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.baidu.android.common.logging.Log;
import com.baidu.android.pushservice.CustomPushNotificationBuilder;
import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.stinfo.pushme.BaseActionBarFragmentActivity;
import com.stinfo.pushme.R;
import com.stinfo.pushme.adapter.CommonPagerAdapter;
import com.stinfo.pushme.common.UserCache;
import com.stinfo.pushme.db.DBAdapter;
import com.stinfo.pushme.entity.Message;

import com.stinfo.pushme.fragment.IndexFragment;
import com.stinfo.pushme.fragment.MessageFragment;
import com.stinfo.pushme.fragment.ContactFragment;
import com.stinfo.pushme.fragment.MoreFragment;
import com.stinfo.pushme.rest.MyStringRequest;
import com.stinfo.pushme.rest.RequestController;
import com.stinfo.pushme.rest.entity.PushAccountReq;
import com.stinfo.pushme.util.PushUtils;

import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

public class MainActivity extends BaseActionBarFragmentActivity implements OnPageChangeListener {
	private static final String TAG = "MainActivity";
	private ArrayList<Fragment> mFragmentsList;
	private CommonPagerAdapter mAdapter;
	private ViewPager mViewPager;

	private RelativeLayout mIndexView = null;
	private RelativeLayout mMessageView = null;
	private RelativeLayout mContactView = null;
	private RelativeLayout mMoreView = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initTabWidget();
		initPagerView();
		initPushService();
		handleIntent(getIntent());
	}

	@Override
	protected void onNewIntent(Intent intent) {
		setIntent(intent);
		handleIntent(intent);
	}

	@Override
	public void onPause() {
		super.onPause();
		RequestController.getInstance().cancelPendingRequests(TAG);
	}

	@Override
	public void onStart() {
		super.onStart();
		PushManager.activityStarted(this);
	}

	@Override
	public void onStop() {
		super.onStop();
		PushManager.activityStoped(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_exit:
			this.finish();
			break;
		}
		return true;
	}

	private void initTabWidget() {
		mIndexView = (RelativeLayout) findViewById(R.id.bottombar_index);
		mMessageView = (RelativeLayout) findViewById(R.id.bottombar_message);
		mContactView = (RelativeLayout) findViewById(R.id.bottombar_contact);
		mMoreView = (RelativeLayout) findViewById(R.id.bottombar_more);

		mIndexView.setOnClickListener(new MyOnClickListener(0));
		mMessageView.setOnClickListener(new MyOnClickListener(1));
		mContactView.setOnClickListener(new MyOnClickListener(2));
		mMoreView.setOnClickListener(new MyOnClickListener(3));
	}

	private void initPagerView() {
		mViewPager = (ViewPager) findViewById(R.id.vp_container);
		mFragmentsList = new ArrayList<Fragment>();

		Fragment indexFrag = IndexFragment.newInstance();
		Fragment messageFrag = MessageFragment.newInstance();
		Fragment contactFrag = ContactFragment.newInstance();
		Fragment moreFrag = MoreFragment.newInstance();

		mFragmentsList.add(indexFrag);
		mFragmentsList.add(messageFrag);
		mFragmentsList.add(contactFrag);
		mFragmentsList.add(moreFrag);

		mAdapter = new CommonPagerAdapter(getSupportFragmentManager(), mFragmentsList);
		mViewPager.setAdapter(mAdapter);
		mViewPager.setOnPageChangeListener(this);
		setCurrentPage(0);
	}

	private class MyOnClickListener implements OnClickListener {
		private int mIndex = 0;

		public MyOnClickListener(int index) {
			mIndex = index;
		}

		@Override
		public void onClick(View v) {
			setCurrentPage(mIndex);
		}
	}

	private void setCurrentPage(int index) {
		mViewPager.setCurrentItem(index);
		switch (index) {
		case 0:
			selectIndex();
			break;
		case 1:
			selectMessage();
			break;
		case 2:
			selectContact();
			break;
		case 3:
			selectMore();
			break;
		}
	}

	private void selectIndex() {
		mIndexView.setSelected(true);
		mMessageView.setSelected(false);
		mContactView.setSelected(false);
		mMoreView.setSelected(false);
	}

	private void selectMessage() {
		mIndexView.setSelected(false);
		mMessageView.setSelected(true);
		mContactView.setSelected(false);
		mMoreView.setSelected(false);
	}

	private void selectContact() {
		mIndexView.setSelected(false);
		mMessageView.setSelected(false);
		mContactView.setSelected(true);
		mMoreView.setSelected(false);
	}

	private void selectMore() {
		mIndexView.setSelected(false);
		mMessageView.setSelected(false);
		mContactView.setSelected(false);
		mMoreView.setSelected(true);
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onPageSelected(int arg0) {
		setCurrentPage(arg0);
	}

	private void initPushService() {
		PushManager.startWork(getApplicationContext(), PushConstants.LOGIN_TYPE_API_KEY,
				PushUtils.getMetaValue(this, "api_key"));
		CustomPushNotificationBuilder builder = new CustomPushNotificationBuilder(
				R.layout.notification_custom_builder, R.id.notification_icon, R.id.notification_title,
				R.id.notification_text);

		builder.setNotificationFlags(Notification.FLAG_AUTO_CANCEL);
		builder.setNotificationDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
		builder.setStatusbarIcon(R.drawable.notification_icon);
		builder.setLayoutDrawable(R.drawable.notification_icon);
		PushManager.setNotificationBuilder(this, 1, builder);
	}

	private void handleIntent(Intent intent) {
		String action = intent.getAction();

		if (PushUtils.ACTION_RESPONSE.equals(action)) {
			String method = intent.getStringExtra(PushUtils.RESPONSE_METHOD);
			if (PushConstants.METHOD_BIND.equals(method)) {
				int errorCode = intent.getIntExtra(PushUtils.RESPONSE_ERRCODE, 0);
				if (errorCode == 0) {
					String content = intent.getStringExtra(PushUtils.RESPONSE_CONTENT);
					successBind(content);
				} else {
					Log.e(TAG, "Bind Fail, Error Code: " + errorCode);
				}
			}
		} else if (PushUtils.ACTION_MESSAGE.equals(action)) {
			String content = intent.getStringExtra(PushUtils.EXTRA_MESSAGE);
			onMessage(content);
		} else {
			Log.i(TAG, "Activity normally start!");
		}
	}

	private void successBind(String content) {
		String pushUserId = "";
		String pushChannelId = "";

		try {
			JSONObject jsonContent = new JSONObject(content);
			JSONObject params = jsonContent.getJSONObject("response_params");
			pushUserId = params.getString("user_id");
			pushChannelId = params.getString("channel_id");
		} catch (JSONException e) {
			Log.e(TAG, "Parse bind json infos error: " + e);
			return;
		}

		UserCache userCache = UserCache.getInstance();
		PushAccountReq reqData = new PushAccountReq();
		reqData.setUserId(userCache.getUserInfo().getUserId());
		reqData.setSessionKey(userCache.getSessionKey());
		reqData.setPushUserId(pushUserId);
		reqData.setPushChannelId(pushChannelId);
		reqData.setDeviceType(3);

		MyStringRequest req = new MyStringRequest(Method.POST, reqData.getAllUrl(),
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						Log.v(TAG, "Response: " + response);
						setTags();
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e(TAG, "Failed to pushAccount request!");
					}
				});

		RequestController.getInstance().addToRequestQueue(req, TAG);
	}

	private void setTags() {
		UserCache userCache = UserCache.getInstance();
		PushManager.setTags(getApplicationContext(), userCache.getTags());
	}

	private void onMessage(String content) {
		Message message = null;
		try {
			message = Message.fromJson(content, 0, 0);
		} catch (JSONException e) {
			Log.e(TAG, "Failed to parse receive content! " + e);
			return;
		}

		DBAdapter dbAdapter = new DBAdapter();
		try {
			dbAdapter.open();
			dbAdapter.addMessage(message);
		} catch (Exception e) {
			Log.e(TAG, "Failed to operate database: " + e);
		} finally {
			dbAdapter.close();
		}
	}
}
package com.stinfo.pushme.activity;

import java.util.ArrayList;
import java.util.Collections;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.stinfo.pushme.MyBaseAdatper;
import com.stinfo.pushme.R;
import com.stinfo.pushme.BaseActionBarActivity;
import com.stinfo.pushme.adapter.GroupListAdapter;
import com.stinfo.pushme.common.AppConstant;
import com.stinfo.pushme.common.UserCache;
import com.stinfo.pushme.db.DBAdapter;
import com.stinfo.pushme.entity.ClassInfo;
import com.stinfo.pushme.entity.Message;
import com.stinfo.pushme.entity.UserComparator;
import com.stinfo.pushme.entity.UserHelper;
import com.stinfo.pushme.entity.UserInfo;
import com.stinfo.pushme.rest.ImageCacheManager;
import com.stinfo.pushme.rest.MyStringRequest;
import com.stinfo.pushme.rest.RequestController;
import com.stinfo.pushme.rest.entity.PushMessageReq;
import com.stinfo.pushme.util.DateTimeUtil;
import com.stinfo.pushme.util.MessageBox;
import com.stinfo.pushme.util.URLChecker;
import com.stinfo.pushme.view.RoundedImageView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ChatActivity extends BaseActionBarActivity implements OnClickListener {
	private static final String TAG = "ChatActivity";
	private LayoutInflater mInflater;
	private ArrayList<Message> mMsgList = new ArrayList<Message>();
	private UserInfo mOtherUser = null;
	private ChatListAdapter mAdapter;
	private ListView lvChatList;
	private EditText etContent;
	private Button btnSend;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat);
		mOtherUser = (UserInfo) getIntent().getSerializableExtra("userInfo");
		
		ActionBar bar = getSupportActionBar();
		bar.setTitle(mOtherUser.getUserName());
		bar.setDisplayHomeAsUpEnabled(true);
		initView();
		initData();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			this.finish();
			Intent intent = new Intent(this, MainActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
			break;
		}
		return true;
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		RequestController.getInstance().cancelPendingRequests(TAG);
	}

	private void initView() {
		mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		etContent = (EditText) findViewById(R.id.et_send_content);
		btnSend = (Button) findViewById(R.id.btn_send);
		btnSend.setOnClickListener(this);
		
		lvChatList = (ListView) findViewById(R.id.lv_chat_list);
		mAdapter = new ChatListAdapter();
		lvChatList.setAdapter(mAdapter);
	}
	
	private void initData() {
		DBAdapter dbAdapter = new DBAdapter();
		mMsgList.clear();

		try {
			dbAdapter.open();
			mMsgList.addAll(dbAdapter.getPersonalMessage(mOtherUser.getUserId()));
			mAdapter.notifyDataSetChanged();
		} catch (Exception e) {
			Log.e(TAG, "Failed to operate database: " + e);
		} finally {
			dbAdapter.close();
		}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_send:
			onSend();
			break;
		}
	}
	
	private void onSend() {
		if (TextUtils.isEmpty(etContent.getText())) {
			MessageBox.showLongMessage(this, "请输入消息！");
			return;
		}
		
		UserCache userCache = UserCache.getInstance();
		String userList = String.format("{ \"userList\": [\"%s\"] }", mOtherUser.getUserId());
		PushMessageReq reqData = new PushMessageReq();
		
		reqData.setUserId(userCache.getUserInfo().getUserId());
		reqData.setSessionKey(userCache.getSessionKey());
		reqData.setUserList(userList);
		reqData.setContent(etContent.getText().toString());
		
		saveMessage(etContent.getText().toString());
		MyStringRequest req = new MyStringRequest(Method.POST, reqData.getAllUrl(),
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						Log.v(TAG, "Response: " + response);
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e(TAG, "Failed to PushMessage request!");
					}
				});

		RequestController.getInstance().addToRequestQueue(req, TAG);
		etContent.setText("");
	}
	
	private void saveMessage(String content) {
		UserCache userCache = UserCache.getInstance();
		Message message = new Message();
		message.setMsgType(AppConstant.MessageType.NORMAL);
		message.setSenderId(userCache.getUserInfo().getUserId());
		message.setReceiverId(mOtherUser.getUserId());
		message.setObjectType(AppConstant.MessageObjectType.PERSONAL);
		message.setGroupType(AppConstant.MessageGroupType.PERSONAL);
		message.setContent(content);
		message.setOutFlag(1);
		message.setCreateTime(DateTimeUtil.getLongTime());
		message.setReadStatus(1);
		message.setSendStatus(AppConstant.MessageSendStatus.SENDING);
		
		mMsgList.add(message);
		mAdapter.notifyDataSetChanged();
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
	
	public class ChatListAdapter extends MyBaseAdatper {
		
		@Override
		public int getCount() {
			return mMsgList.size();
		}

		@Override
		public Object getItem(int pos) {
			return mMsgList.get(pos);
		}

		@Override
		public long getItemId(int pos) {
			return pos;
		}

		@Override
		public View getView(int pos, View convertView, ViewGroup parentView) {
			TextFromHolder fromHolder = null;
			TextToHolder toHolder = null;
			int outFlag = getItemViewType(pos);
			
			if (convertView == null) {
				switch (outFlag) {
				case 0:
					fromHolder = new TextFromHolder();
					convertView = mInflater.inflate(R.layout.chatting_item_from, null);
					fromHolder.layoutTime = (LinearLayout) convertView.findViewById(R.id.layout_send_time);
					fromHolder.tvSendTime = (TextView) convertView.findViewById(R.id.tv_chatting_send_time);
					fromHolder.ivAvatar = (RoundedImageView) convertView.findViewById(R.id.iv_chatting_avatar);
					fromHolder.tvContent = (TextView) convertView.findViewById(R.id.tv_chatting_content);
					bindFromData(fromHolder, pos);
					convertView.setTag(fromHolder);
					break;
				case 1:
					toHolder = new TextToHolder();
					convertView = mInflater.inflate(R.layout.chatting_item_to, null);
					toHolder.layoutTime = (LinearLayout) convertView.findViewById(R.id.layout_send_time);
					toHolder.tvSendTime = (TextView) convertView.findViewById(R.id.tv_chatting_send_time);
					toHolder.ivAvatar = (RoundedImageView) convertView.findViewById(R.id.iv_chatting_avatar);
					toHolder.tvContent = (TextView) convertView.findViewById(R.id.tv_chatting_content);
					toHolder.ivChattingState = (ImageView) convertView.findViewById(R.id.iv_chatting_state);
					toHolder.pbUploading = (ProgressBar) convertView.findViewById(R.id.pb_uploading);
					bindToData(toHolder, pos);
					convertView.setTag(toHolder);
					break;
				}
			} else {
				switch (outFlag) {
				case 0:
					fromHolder=(TextFromHolder) convertView.getTag();
					bindFromData(fromHolder, pos);
					break;
				case 1:
					toHolder=(TextToHolder) convertView.getTag();
					bindToData(toHolder, pos);
					break;
				}
			}
		
			return convertView;
		}
		
		private void bindFromData(TextFromHolder fromHolder, int pos) {
			Message message = mMsgList.get(pos);
			fromHolder.tvContent.setText(message.getContent());
			fromHolder.tvSendTime.setText(DateTimeUtil.toStandardTime(message.getCreateTime()));
			
			try {
				if (URLChecker.isUrl(mOtherUser.getPicUrl())) {
					Log.d(TAG, "loadImage: " + mOtherUser.getPicUrl());
					ImageListener listener = ImageLoader.getImageListener(fromHolder.ivAvatar,
							UserHelper.getDefaultImage(mOtherUser), UserHelper.getErrorImage(mOtherUser));
					ImageCacheManager.getInstance().getImageLoader().get(mOtherUser.getPicUrl(), listener);
				}
			} catch (Exception e) {
				Log.e(TAG, "Failed to loadImage: " + mOtherUser.getPicUrl());
			}
		}
		
		private void bindToData(TextToHolder toHolder, int pos) {
			Message message = mMsgList.get(pos);
			toHolder.tvContent.setText(message.getContent());
			toHolder.tvSendTime.setText(DateTimeUtil.toStandardTime(message.getCreateTime()));
			UserInfo userInfo = UserCache.getInstance().getUserInfo();
			
			try {
				if (URLChecker.isUrl(userInfo.getPicUrl())) {
					Log.d(TAG, "loadImage: " + userInfo.getPicUrl());
					ImageListener listener = ImageLoader.getImageListener(toHolder.ivAvatar,
							UserHelper.getDefaultImage(userInfo), UserHelper.getErrorImage(userInfo));
					ImageCacheManager.getInstance().getImageLoader().get(userInfo.getPicUrl(), listener);
				}
			} catch (Exception e) {
				Log.e(TAG, "Failed to loadImage: " + mOtherUser.getPicUrl());
			}
		}
		
		@Override
		public int getItemViewType(int pos) {
			Message message = mMsgList.get(pos);
			int outFlag = message.getOutFlag();
			return outFlag;
		}
		
		@Override
		public int getViewTypeCount() {
			return 2;
		}

		private final class TextFromHolder {
			public LinearLayout layoutTime;
			public TextView tvSendTime;
			public RoundedImageView ivAvatar;
			public TextView tvContent;
		}
		
		private final class TextToHolder {
			public LinearLayout layoutTime;
			public TextView tvSendTime;
			public RoundedImageView ivAvatar;
			public TextView tvContent;
			public ImageView ivChattingState;
			public ProgressBar pbUploading;
		}
	}
}

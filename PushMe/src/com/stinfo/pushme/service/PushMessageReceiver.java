package com.stinfo.pushme.service;

import com.baidu.android.pushservice.PushConstants;
import com.stinfo.pushme.activity.MainActivity;
import com.stinfo.pushme.util.PushUtils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class PushMessageReceiver extends BroadcastReceiver {
	private static final String TAG = "PushMessageReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d(TAG, ">>> Receive intent: \r\n" + intent);

		if (intent.getAction().equals(PushConstants.ACTION_MESSAGE)) {

			String message = intent.getExtras().getString(PushConstants.EXTRA_PUSH_MESSAGE_STRING);
			Log.d(TAG, "onMessage: " + message);
			Log.d(TAG, "EXTRA_EXTRA = " + intent.getStringExtra(PushConstants.EXTRA_EXTRA));

			Intent responseIntent = new Intent(PushUtils.ACTION_MESSAGE);
			responseIntent.putExtra(PushUtils.EXTRA_MESSAGE, message);
			responseIntent.setClass(context, MainActivity.class);
			responseIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(responseIntent);

		} else if (intent.getAction().equals(PushConstants.ACTION_RECEIVE)) {

			String method = intent.getStringExtra(PushConstants.EXTRA_METHOD);
			int errorCode = intent.getIntExtra(PushConstants.EXTRA_ERROR_CODE, PushConstants.ERROR_SUCCESS);
			String content = "";
			if (intent.getByteArrayExtra(PushConstants.EXTRA_CONTENT) != null) {
				content = new String(intent.getByteArrayExtra(PushConstants.EXTRA_CONTENT));
			}

			Log.d(TAG, "onMessage: method : " + method);
			Log.d(TAG, "onMessage: result : " + errorCode);
			Log.d(TAG, "onMessage: content : " + content);

			Intent responseIntent = new Intent(PushUtils.ACTION_RESPONSE);
			responseIntent.putExtra(PushUtils.RESPONSE_METHOD, method);
			responseIntent.putExtra(PushUtils.RESPONSE_ERRCODE, errorCode);
			responseIntent.putExtra(PushUtils.RESPONSE_CONTENT, content);
			responseIntent.setClass(context, MainActivity.class);
			responseIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(responseIntent);

		} else if (intent.getAction().equals(PushConstants.ACTION_RECEIVER_NOTIFICATION_CLICK)) {

			Log.d(TAG, "intent = " + intent.toUri(0));
			Log.d(TAG, "EXTRA_EXTRA = " + intent.getStringExtra(PushConstants.EXTRA_EXTRA));
			Log.d(TAG, "title = " + intent.getStringExtra(PushConstants.EXTRA_NOTIFICATION_TITLE));
			Log.d(TAG, "content = " + intent.getStringExtra(PushConstants.EXTRA_NOTIFICATION_CONTENT));
	
		}
	}
}

package com.stinfo.pushme.util;

import java.util.HashMap;
import com.stinfo.pushme.R;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.widget.Toast;

public class MessageBox {
	private static final String TAG = "LoginActivity";
	
	public static void showNetworkError(Context context) {
		Resources res = context.getResources();
		String errInfo = res.getString(R.string.network_offline);
		Toast.makeText(context, errInfo, Toast.LENGTH_LONG).show();
	}

	public static void showServerError(Context context) {
		Resources res = context.getResources();
		String errInfo = res.getString(R.string.server_exception);
		Toast.makeText(context, errInfo, Toast.LENGTH_LONG).show();
	}

	public static void showAckError(Context context, int ack) {
		Resources res = context.getResources();
		HashMap<String, String> ackMap = new HashMap<String, String>();
		String[] items = res.getStringArray(R.array.ack_list);
		for (int i = 0; i < items.length; i++) {
			try {
				MapItem mapItem = new MapItem(items[i]);
				Log.d("ID", mapItem.getID());
				Log.d("value", mapItem.getValue());
				ackMap.put(mapItem.getID(), mapItem.getValue());
			} catch (Exception ex) {
				Log.e(TAG, "Invalid acklist array data!");
				break;
			}
		}
		
		String errInfo = ackMap.get(String.valueOf(ack));
		Toast.makeText(context, errInfo, Toast.LENGTH_LONG).show();
	}

	public static void showParserError(Context context) {
		Resources res = context.getResources();
		String errInfo = res.getString(R.string.parser_exception);
		Toast.makeText(context, errInfo, Toast.LENGTH_LONG).show();
	}

	public static void showMessage(Context context, String message) {
		Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
	}

	public static void showLongMessage(Context context, String message) {
		Toast.makeText(context, message, Toast.LENGTH_LONG).show();
	}
}

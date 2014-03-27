package com.stinfo.pushme.common;

import com.stinfo.pushme.MyApplication;

import android.content.Context;
import android.content.SharedPreferences;

public class AppConfig {
	private static AppConfig mInstance = null;
	private final static String DB_NAME = "config.pref";
	private SharedPreferences.Editor mEditor;
	private SharedPreferences mSettings;

	private AppConfig() {
		Context context = MyApplication.getInstance().getApplicationContext();
		mSettings = context.getSharedPreferences(DB_NAME, 0);
		mEditor = mSettings.edit();
	}

	public static synchronized AppConfig getInstance() {
		if (mInstance == null) {
			mInstance = new AppConfig();
		}
		return mInstance;
	}

	public boolean getFirstRun() {
		return mSettings.getBoolean("firstRun", true);
	}

	public void setFirstRun(boolean value) {
		mEditor.putBoolean("firstRun", value);
		mEditor.commit();
	}
}

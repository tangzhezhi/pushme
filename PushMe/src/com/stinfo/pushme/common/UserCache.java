package com.stinfo.pushme.common;

import java.util.ArrayList;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.stinfo.pushme.MyApplication;
import com.stinfo.pushme.entity.ClassInfo;
import com.stinfo.pushme.entity.Parent;
import com.stinfo.pushme.entity.SerializeHelper;
import com.stinfo.pushme.entity.Student;
import com.stinfo.pushme.entity.Teacher;
import com.stinfo.pushme.entity.TeacherRole;
import com.stinfo.pushme.entity.UserInfo;

public final class UserCache {
	private static final String TAG = "UserCache";

	private static UserCache mInstance = null;
	private final static String DB_NAME = "logon.pref";
	private SharedPreferences.Editor mEditor;
	private SharedPreferences mSettings;
	private Context mContext;
	private ArrayList<String> mTags = new ArrayList<String>();

	private boolean logon = false;
	private String sessionKey = "";
	private String password = "";
	private UserInfo userInfo = null;
	private ClassInfo currentClass = null;
	private Student currentChild = null;

	private UserCache() {
		mContext = MyApplication.getInstance().getApplicationContext();
		mSettings = mContext.getSharedPreferences(DB_NAME, 0);
		mEditor = mSettings.edit();

		initUserCache();
	}

	public static synchronized UserCache getInstance() {
		if (mInstance == null) {
			mInstance = new UserCache();
		}
		return mInstance;
	}

	private void initUserCache() {
		logon = mSettings.getBoolean("logon", false);
		if (logon) {
			try {
				sessionKey = mSettings.getString("sessionKey", "STINFO01");
				password = mSettings.getString("password", "****");
				userInfo = SerializeHelper.restoreUserInfo(mContext);
				currentClass = SerializeHelper.restoreCurrentClass(mContext);
				if (userInfo instanceof Parent) {
					currentChild = SerializeHelper.restoreCurrentChild(mContext);
				}
				updateTags();
			} catch (Exception e) {
				Log.e(TAG, "Failed to deserialize cache file! " + e.toString());
				logon = false;
			}
		}
	}

	public boolean isLogon() {
		return logon;
	}

	public void setLogon(boolean value) {
		this.logon = value;
		mEditor.putBoolean("logon", value);
		mEditor.commit();
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
		mEditor.putString("password", password);
		mEditor.commit();
	}

	public String getSessionKey() {
		return sessionKey;
	}

	public void setSessionKey(String sessionKey) {
		this.sessionKey = sessionKey;
		mEditor.putString("sessionKey", sessionKey);
		mEditor.commit();
	}

	public UserInfo getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
		SerializeHelper.saveUserInfo(mContext, userInfo);
		updateTags();
	}

	public ClassInfo getCurrentClass() {
		return currentClass;
	}

	public void setCurrentClass(ClassInfo currentClass) {
		this.currentClass = currentClass;
		SerializeHelper.saveCurrentClass(mContext, currentClass);
	}

	public Student getCurrentChild() {
		return currentChild;
	}

	public void setCurrentChild(Student currentChild) {
		this.currentChild = currentChild;
		SerializeHelper.saveCurrentChild(mContext, currentChild);
	}

	public ArrayList<String> getTags() {
		return mTags;
	}

	private void updateTags() {
		if (userInfo == null) {
			return;
		}
		
		mTags.clear();
		if (userInfo instanceof Student) {
			Student student = (Student) userInfo;
			mTags.add("C" + student.getClassId() + "_1");
			mTags.add("C" + student.getClassId() + "_4");
		} else if (userInfo instanceof Parent) {
			Parent parent = (Parent) userInfo;
			for (Student student : parent.getChildList()) {
				mTags.add("C" + student.getClassId() + "_2");
				mTags.add("C" + student.getClassId() + "_4");
			}
		} else if (userInfo instanceof Teacher) {
			Teacher teacher = (Teacher) userInfo;
			for (TeacherRole role : teacher.getTeacherRoleList()) {
				mTags.add("C" + role.getClassId() + "_3");
				mTags.add("C" + role.getClassId() + "_4");
			}
		}
	}
}

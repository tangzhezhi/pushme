package com.stinfo.pushme.entity;

import java.io.Serializable;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import com.stinfo.pushme.util.PinYinUtil;

public abstract class UserInfo implements Serializable {
	private static final long serialVersionUID = 5541050827738663565L;
	
	private String userId = "";
	private String userName = "";
	private int sex = 0;
	private String phone = "";
	private String picUrl = "";
	private String pinYin = "";
	private String initial = "0";
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	@SuppressLint("DefaultLocale")
	public void setUserName(String userName) {
		this.userName = userName.trim();
		this.pinYin = PinYinUtil.getPinYin(this.userName);
		if (TextUtils.isEmpty(this.pinYin)) {
			this.initial = "0";
		} else {
			this.initial = String.valueOf(this.pinYin.charAt(0));
		}
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}
	
	public String getPinYin() {
		return pinYin;
	}

	public String getInitial() {
		return initial;
	}

	@Override
	public String toString() {
		return userName;
	}
}

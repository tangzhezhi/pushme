package com.stinfo.pushme.rest.entity;

import java.util.HashMap;

import com.stinfo.pushme.common.AppConstant;

public class UserLoginReq extends BaseRequest {
	private String userId = "";
	private String password = "";

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String getPath() {
		return AppConstant.BASE_URL + "userLogin.aspx";
	}

	@Override
	public HashMap<String, String> toParamsMap() {
		HashMap<String, String> paramsHashMap = new HashMap<String, String>();
		paramsHashMap.put("userId", this.userId);
		paramsHashMap.put("password", this.password);
		return paramsHashMap;
	}
}

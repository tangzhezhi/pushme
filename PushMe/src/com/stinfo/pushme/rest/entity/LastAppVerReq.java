package com.stinfo.pushme.rest.entity;

import java.util.HashMap;

import com.stinfo.pushme.common.AppConstant;

public class LastAppVerReq extends BaseRequest {

	private String appName = "";
	private int appType = 1;

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public int getAppType() {
		return appType;
	}

	public void setAppType(int appType) {
		this.appType = appType;
	}

	@Override
	public String getPath() {
		return AppConstant.BASE_URL + "lastAppVer.aspx";
	}

	@Override
	public HashMap<String, String> toParamsMap() {
		HashMap<String, String> paramsHashMap = new HashMap<String, String>();
		paramsHashMap.put("appName", this.appName);
		paramsHashMap.put("appType", String.valueOf(this.appType));
		return paramsHashMap;
	}
}

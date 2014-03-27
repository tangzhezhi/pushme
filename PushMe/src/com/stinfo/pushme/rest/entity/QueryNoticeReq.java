package com.stinfo.pushme.rest.entity;

import java.util.HashMap;

import com.stinfo.pushme.common.AppConstant;

public class QueryNoticeReq extends BaseRequest {
	private String userId = "";
	private String sessionKey = "";
	private int queryType = 0;
	private String queryValue = "";
	private String lastCreateTime = "";

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getSessionKey() {
		return sessionKey;
	}

	public void setSessionKey(String sessionKey) {
		this.sessionKey = sessionKey;
	}

	public int getQueryType() {
		return queryType;
	}

	public void setQueryType(int queryType) {
		this.queryType = queryType;
	}

	public String getQueryValue() {
		return queryValue;
	}

	public void setQueryValue(String queryValue) {
		this.queryValue = queryValue;
	}

	public String getLastCreateTime() {
		return lastCreateTime;
	}

	public void setLastCreateTime(String lastCreateTime) {
		this.lastCreateTime = lastCreateTime;
	}

	@Override
	public String getPath() {
		return AppConstant.BASE_URL + "queryNotice.aspx";
	}

	@Override
	public HashMap<String, String> toParamsMap() {
		HashMap<String, String> paramsHashMap = new HashMap<String, String>();
		paramsHashMap.put("userId", this.userId);
		paramsHashMap.put("sessionKey", this.sessionKey);
		paramsHashMap.put("queryType", String.valueOf(this.queryType));
		paramsHashMap.put("queryValue", this.queryValue);
		paramsHashMap.put("lastCreateTime", this.lastCreateTime);
		return paramsHashMap;
	}
}

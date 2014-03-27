package com.stinfo.pushme.rest.entity;

import java.util.HashMap;

import com.stinfo.pushme.common.AppConstant;

public class PushGroupMessageReq extends BaseRequest {
	private String userId = "";
	private String sessionKey = "";
	private int objectType = 0;
	private String classId = "";
	private String content = "";

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

	public int getObjectType() {
		return objectType;
	}

	public void setObjectType(int objectType) {
		this.objectType = objectType;
	}

	public String getClassId() {
		return classId;
	}

	public void setClassId(String classId) {
		this.classId = classId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String getPath() {
		return AppConstant.BASE_URL + "pushGroupMsg.aspx";
	}

	@Override
	public HashMap<String, String> toParamsMap() {
		HashMap<String, String> paramsHashMap = new HashMap<String, String>();
		paramsHashMap.put("userId", this.userId);
		paramsHashMap.put("sessionKey", this.sessionKey);
		paramsHashMap.put("objectType", String.valueOf(this.objectType));
		paramsHashMap.put("classId", this.classId);
		paramsHashMap.put("content", this.content);
		return paramsHashMap;
	}
}

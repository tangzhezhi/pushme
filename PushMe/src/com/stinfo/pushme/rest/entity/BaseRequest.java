package com.stinfo.pushme.rest.entity;

import java.util.HashMap;
import java.util.Map;

import android.text.TextUtils;

public abstract class BaseRequest {
	public abstract String getPath();

	public abstract HashMap<String, String> toParamsMap();

	public String getAllUrl() {
		Map<String, String> paramsMap = toParamsMap();
		StringBuilder paramsStr = new StringBuilder();
		
		for (Map.Entry<String, String> entry : paramsMap.entrySet()) {
			paramsStr.append(entry.getKey());
			paramsStr.append('=');
			paramsStr.append(entry.getValue());
			paramsStr.append('&');
		}

		if (!TextUtils.isEmpty(paramsStr.toString())) {
			return getPath() + "?" + paramsStr.toString();
		} else {
			return getPath();
		}
	}
}

package com.stinfo.pushme.rest.entity;

import org.json.JSONException;
import org.json.JSONObject;

public class BaseResponse {
	private int ack = 0;

    public int getAck() {
        return ack;
    }

    public BaseResponse(String jsonStr) throws JSONException {
    	parseResponseData(jsonStr);
    }

    private void parseResponseData(String jsonStr) throws JSONException {
    	JSONObject rootObj = new JSONObject(jsonStr);
        this.ack = rootObj.getInt("ack");
    }
}

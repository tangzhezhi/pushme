package com.stinfo.pushme.rest.entity;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.stinfo.pushme.entity.Daily;

public class QueryDailyResp extends BaseResponse {
	private int count = 0;
	private ArrayList<Daily> dailyList = new ArrayList<Daily>();

	public int getCount() {
		return count;
	}

	public ArrayList<Daily> getDailyList() {
		return dailyList;
	}

	public QueryDailyResp(String jsonStr) throws JSONException  {
		super(jsonStr);;
		parseResponseData(jsonStr);
	}

	private void parseResponseData(String jsonStr) throws JSONException {
		JSONObject rootObj = new JSONObject(jsonStr);
		if (rootObj.has("response")) {
			this.count = rootObj.getInt("count");
			JSONObject respObj = rootObj.getJSONObject("response");
			JSONArray dailyArray = respObj.getJSONArray("dataList");
			for (int i = 0; i < dailyArray.length(); i++) {
				JSONObject dailyObj = dailyArray.getJSONObject(i);
				Daily daily = new Daily();

				daily.setAuthorId(dailyObj.getString("authorId"));
				daily.setAuthorName(dailyObj.getString("authorName"));
				daily.setReceiverId(dailyObj.getString("receiverId"));
				daily.setReceiverName(dailyObj.getString("receiverName"));
				daily.setContent(dailyObj.getString("content"));
				daily.setCreateTime(dailyObj.getString("createTime"));
				this.dailyList.add(daily);
			}
		}
	}
}

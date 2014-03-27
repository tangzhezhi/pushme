package com.stinfo.pushme.rest.entity;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.stinfo.pushme.entity.Notice;

public class QueryNoticeResp extends BaseResponse {
	private int count = 0;
	private ArrayList<Notice> noticeList = new ArrayList<Notice>();

	public int getCount() {
		return count;
	}

	public ArrayList<Notice> getNoticeList() {
		return noticeList;
	}

	public QueryNoticeResp(String jsonStr) throws JSONException  {
		super(jsonStr);
		parseResponseData(jsonStr);
	}

	private void parseResponseData(String jsonStr) throws JSONException {
		JSONObject rootObj = new JSONObject(jsonStr);
		if (rootObj.has("response")) {
			this.count = rootObj.getInt("count");
			JSONObject respObj = rootObj.getJSONObject("response");
			JSONArray noticeArray = respObj.getJSONArray("dataList");
			for (int i = 0; i < noticeArray.length(); i++) {
				JSONObject noticeObj = noticeArray.getJSONObject(i);
				Notice notice = new Notice();

				notice.setAuthorId(noticeObj.getString("authorId"));
				notice.setAuthorName(noticeObj.getString("authorName"));
				notice.setType(noticeObj.getInt("type"));
				notice.setObjectId(noticeObj.getString("objectId"));
				notice.setTitle(noticeObj.getString("title"));
				notice.setContent(noticeObj.getString("content"));
				notice.setCreateTime(noticeObj.getString("createTime"));
				this.noticeList.add(notice);
			}
		}
	}
}

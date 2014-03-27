package com.stinfo.pushme.rest.entity;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.stinfo.pushme.entity.ClassInfo;
import com.stinfo.pushme.entity.ParentRoster;

public class ParentListResp {
	private int ack = 0;
	private ArrayList<ParentRoster> parentList = new ArrayList<ParentRoster>();
	private ClassInfo classInfo;

    public int getAck() {
        return ack;
    }
		
	public ArrayList<ParentRoster> getParentList() {
		return parentList;
	}

	public ParentListResp(String jsonStr, ClassInfo classInfo) throws JSONException {
		this.classInfo = classInfo;
		parseParentRoster(jsonStr);
	}
	
	private void parseParentRoster(String jsonStr) throws JSONException {
		JSONObject rootObj = new JSONObject(jsonStr);
		this.ack = rootObj.getInt("ack");
		
		if (rootObj.has("response")) {
			JSONObject respObj = rootObj.getJSONObject("response");
			JSONArray userArray = respObj.getJSONArray("parentList");
			for (int i = 0; i < userArray.length(); i++) {
				JSONObject userObj = userArray.getJSONObject(i);
				ParentRoster parentRoster = new ParentRoster();
				
				parentRoster.setUserId(userObj.getString("userId"));
				parentRoster.setUserName(userObj.getString("userName"));
				parentRoster.setSex(userObj.getInt("sex"));
				parentRoster.setPhone(userObj.getString("phone"));
				parentRoster.setPicUrl(userObj.getString("picUrl"));
				parentRoster.setChildUserId(userObj.getString("childUserId"));
				parentRoster.setChildName(userObj.getString("childName"));
				parentRoster.setSchoolId(this.classInfo.getSchoolId());
				parentRoster.setClassId(this.classInfo.getClassId());
				parentRoster.setClassName(this.classInfo.getClassName());
				this.parentList.add(parentRoster);
			}
		}
	}
}

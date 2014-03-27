package com.stinfo.pushme.rest.entity;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.stinfo.pushme.entity.TeacherRoster;

public class SchoolTeacherListResp {
	private int ack = 0;
	private ArrayList<TeacherRoster> teacherList = new ArrayList<TeacherRoster>();
	private String schoolId;

	public int getAck() {
		return ack;
	}

	public ArrayList<TeacherRoster> getTeacherList() {
		return teacherList;
	}

	public SchoolTeacherListResp(String jsonStr, String schoolId) throws JSONException {
		this.schoolId = schoolId;
		parseTeacherRoster(jsonStr);
	}

	private void parseTeacherRoster(String jsonStr) throws JSONException {
		JSONObject rootObj = new JSONObject(jsonStr);
		this.ack = rootObj.getInt("ack");

		if (rootObj.has("response")) {
			JSONObject respObj = rootObj.getJSONObject("response");
			JSONArray userArray = respObj.getJSONArray("teacherList");
			for (int i = 0; i < userArray.length(); i++) {
				JSONObject userObj = userArray.getJSONObject(i);
				TeacherRoster teacherRoster = new TeacherRoster();

				teacherRoster.setUserId(userObj.getString("userId"));
				teacherRoster.setUserName(userObj.getString("userName"));
				teacherRoster.setTeacherId(userObj.getString("teacherId"));
				teacherRoster.setSex(userObj.getInt("sex"));
				teacherRoster.setPhone(userObj.getString("phone"));
				teacherRoster.setPicUrl(userObj.getString("picUrl"));
				teacherRoster.setRole(userObj.getString("role"));
				teacherRoster.setClassId(userObj.getString("classId"));
				teacherRoster.setClassName(userObj.getString("className"));
				teacherRoster.setSchoolId(this.schoolId);
				this.teacherList.add(teacherRoster);
			}
		}
	}
}

package com.stinfo.pushme.rest.entity;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.stinfo.pushme.entity.ClassInfo;
import com.stinfo.pushme.entity.StudentRoster;

public class StudentListResp {
	private int ack = 0;
	private ArrayList<StudentRoster> studentList = new ArrayList<StudentRoster>();
	private ClassInfo classInfo;

	public int getAck() {
		return ack;
	}

	public ArrayList<StudentRoster> getStudentList() {
		return studentList;
	}

	public StudentListResp(String jsonStr, ClassInfo classInfo) throws JSONException {
		this.classInfo = classInfo;
		parseStudentRoster(jsonStr);
	}

	private void parseStudentRoster(String jsonStr) throws JSONException {
		JSONObject rootObj = new JSONObject(jsonStr);
		this.ack = rootObj.getInt("ack");

		if (rootObj.has("response")) {
			JSONObject respObj = rootObj.getJSONObject("response");
			JSONArray userArray = respObj.getJSONArray("studentList");
			for (int i = 0; i < userArray.length(); i++) {
				JSONObject userObj = userArray.getJSONObject(i);
				StudentRoster student = new StudentRoster();

				student.setUserId(userObj.getString("userId"));
				student.setUserName(userObj.getString("userName"));
				student.setStudentNo(userObj.getString("studentNo"));
				student.setSex(userObj.getInt("sex"));
				student.setPhone(userObj.getString("phone"));
				student.setPicUrl(userObj.getString("picUrl"));
				student.setSchoolId(this.classInfo.getSchoolId());
				student.setClassId(this.classInfo.getClassId());
				student.setClassName(this.classInfo.getClassName());
				this.studentList.add(student);
			}
		}
	}
}

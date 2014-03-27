package com.stinfo.pushme.rest.entity;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;
import android.util.Log;

import com.stinfo.pushme.common.AppConstant;
import com.stinfo.pushme.entity.Parent;
import com.stinfo.pushme.entity.Student;
import com.stinfo.pushme.entity.SystemUser;
import com.stinfo.pushme.entity.Teacher;
import com.stinfo.pushme.entity.TeacherRole;
import com.stinfo.pushme.entity.UserInfo;

public class UserInfoParser {
	private static final String TAG = "UserInfoParser";
	private UserInfo userInfo = null;

	public UserInfo getUserInfo() {
		return userInfo;
	}

	public UserInfoParser(int userType, String userInfo) throws JSONException {
		Log.d(TAG, "Begin UserInfoParser! \tuserType=" + String.valueOf(userType) + "|userInfo=" + userInfo);
		if (TextUtils.isEmpty(userInfo)) {
			throw new JSONException("It's invalid json data");
		}

		JSONObject userObj = new JSONObject(userInfo);
		if (userType == AppConstant.UserType.SYSTEM) {
			this.userInfo = parseSysUserInfo(userObj);
		} else if (userType == AppConstant.UserType.STUDENT) {
			this.userInfo = parseStudentInfo(userObj);
		} else if (userType == AppConstant.UserType.PARENT) {
			this.userInfo = parseParentInfo(userObj);
		} else if (userType == AppConstant.UserType.TEACHER) {
			this.userInfo = parseTeacherInfo(userObj);
		} else {
			throw new JSONException("Invalid userType!");
		}
	}

	private SystemUser parseSysUserInfo(JSONObject userObj) throws JSONException {
		SystemUser sysUser = new SystemUser();
		sysUser.setUserId(userObj.getString("userId"));
		sysUser.setUserName(userObj.getString("userName"));
		sysUser.setPicUrl(userObj.getString("picUrl"));
		return sysUser;
	}

	private Student parseStudentInfo(JSONObject userObj) throws JSONException {
		Student student = new Student();

		student.setUserId(userObj.getString("userId"));
		student.setUserName(userObj.getString("userName"));
		student.setSchoolId(userObj.getString("schoolId"));
		student.setStudentNo(userObj.getString("studentNo"));
		student.setSex(userObj.getInt("sex"));
		student.setPhone(userObj.getString("phone"));
		student.setPicUrl(userObj.getString("picUrl"));
		JSONObject classObj = new JSONObject(userObj.getString("class"));
		student.setClassId(classObj.getString("classId"));
		student.setClassName(classObj.getString("className"));
		return student;
	}

	private Parent parseParentInfo(JSONObject userObj) throws JSONException {
		Parent parent = new Parent();

		parent.setUserId(userObj.getString("userId"));
		parent.setUserName(userObj.getString("userName"));
		parent.setSex(userObj.getInt("sex"));
		parent.setPhone(userObj.getString("phone"));
		parent.setPicUrl(userObj.getString("picUrl"));
		JSONArray childArray = userObj.getJSONArray("child");
		for (int i = 0; i < childArray.length(); i++) {
			Student child = parseStudentInfo(childArray.getJSONObject(i));
			parent.addOrderItem(child);
		}

		return parent;
	}

	private Teacher parseTeacherInfo(JSONObject userObj) throws JSONException {
		Teacher teacher = new Teacher();

		teacher.setUserId(userObj.getString("userId"));
		teacher.setUserName(userObj.getString("userName"));
		teacher.setSchoolId(userObj.getString("schoolId"));
		teacher.setTeacherId(userObj.getString("teacherId"));
		teacher.setSex(userObj.getInt("sex"));
		teacher.setPhone(userObj.getString("phone"));
		teacher.setPicUrl(userObj.getString("picUrl"));

		JSONArray roleArray = userObj.getJSONArray("class");
		teacher.setTeacherRoleList(parseTeacherRole(roleArray));
		return teacher;
	}

	private ArrayList<TeacherRole> parseTeacherRole(JSONArray roleArray) throws JSONException {
		ArrayList<TeacherRole> teacherRoleList = new ArrayList<TeacherRole>();
		for (int i = 0; i < roleArray.length(); i++) {
			TeacherRole roleItem = new TeacherRole();
			JSONObject itemObj = roleArray.getJSONObject(i);

			roleItem.setClassId(itemObj.getString("classId"));
			roleItem.setClassName(itemObj.getString("className"));
			roleItem.setRole(itemObj.getString("role"));
			teacherRoleList.add(roleItem);
		}

		return teacherRoleList;
	}
}

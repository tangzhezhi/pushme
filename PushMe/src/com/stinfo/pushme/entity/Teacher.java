package com.stinfo.pushme.entity;

import java.io.Serializable;
import java.util.ArrayList;

public class Teacher extends UserInfo implements Serializable {
	private static final long serialVersionUID = 1141575949118732239L;

	private String teacherId;
	private String schoolId;
	private ArrayList<TeacherRole> teacherRoleList = new ArrayList<TeacherRole>();

	public String getSchoolId() {
		return schoolId;
	}
	
	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}

	public String getTeacherId() {
		return teacherId;
	}
	
	public void setTeacherId(String teacherId) {
		this.teacherId = teacherId;
	}

	public ArrayList<TeacherRole> getTeacherRoleList() {
		return teacherRoleList;
	}

	public void setTeacherRoleList(ArrayList<TeacherRole> list) {
		this.teacherRoleList = list;
	}

	public void addTeacherRoleList(ArrayList<TeacherRole> list) {
		this.teacherRoleList.addAll(list);
	}

	public void addTeacherRoleItem(TeacherRole item) {
		this.teacherRoleList.add(item);
	}
}

package com.stinfo.pushme.entity;

import java.io.Serializable;

public class TeacherRole implements Serializable {
	private static final long serialVersionUID = 7695603770925424909L;
	
	private String classId;
	private String className;
	private String role;
	
	public String getClassId() {
		return classId;
	}

	public void setClassId(String classId) {
		this.classId = classId;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
}
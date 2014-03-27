package com.stinfo.pushme.common;

public class AppConstant {
	public static final String BASE_URL = "http://113.247.250.200:8090/";
	public static final int DEFAULT_PAGESIZE = 15;

	public static final class UserType {
		public static final int SYSTEM = 0;
		public static final int STUDENT = 1;
		public static final int PARENT = 2;
		public static final int TEACHER = 3;
	}

	public static final class Ack {
		public static final int SUCCESS = 0;
		public static final int NOT_FOUND = 100;
	}
	
	public static final class Sex {
		public static final int FEMALE = 1;
		public static final int MALE = 0;
	}
	
	public static final class NoticeQueryType {
		public static final int QUERY_ALL = 0;
		public static final int QUERY_SCHOOL = 1;
		public static final int QUERY_CLASS = 2;
		public static final int QUERY_SOMEONE = 3;
	}
	
	public static final class NoticeType {
		public static final int SCHOOL = 1;
		public static final int CLASS= 2;
	}
	
	public static final class HomeworkQueryType {
		public static final int QUERY_CLASS = 1;
		public static final int QUERY_SOMEONE = 2;
	}
	
	public static final class MessageType {
		public static final int NORMAL = 1;
		public static final int NOTICE = 2;
		public static final int HOMEWORK = 3;
		public static final int DAILY = 4;
	}
	
	public static final class MessageObjectType {
		public static final int PERSONAL = 0;
		public static final int STUDENT = 1;
		public static final int PARENT = 2;
		public static final int TEACHER = 3;
		public static final int ALL = 4;
	}
	
	public static final class MessageGroupType {
		public static final int PERSONAL = 0;
		public static final int SCHOOL = 1;
		public static final int CLASS = 2;
	}
	
	public static final class MessageSendStatus {
		public static final int SENDING = 1;
		public static final int SUCCESS = 2;
		public static final int FAILED = 3;
	}
}

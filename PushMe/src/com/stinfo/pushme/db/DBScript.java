package com.stinfo.pushme.db;

public final class DBScript {
    
	public static final String CREATE_TABLE_NOTICE = 
    		"CREATE TABLE Notice " + 
            "( " + 
            	"cacheUserId text not null, " +
                "authorId text not null, " + 
                "authorName text not null, " + 
                "type integer not null, " + 
                "objectId text not null, " +
                "title text not null, " +
                "content text not null, " +
                "createTime text not null " +
            "); ";
   
    public static final String CREATE_TABLE_HOMEWORK = 
    		"CREATE TABLE Homework " + 
            "( " + 
            	"cacheUserId text not null, " +
                "authorId text not null, " + 
                "authorName text not null, " + 
                "subject text not null, " + 
                "classId text not null, " +
                "content text not null, " +
                "createTime text not null " +
            "); ";
	
    public static final String CREATE_TABLE_DAILY = 
    		"CREATE TABLE Daily " +  
    		"( " + 
    			"cacheUserId text not null, " + 
            	"authorId text not null, " + 
	            "authorName text not null, " + 
	            "receiverId text not null, " + 
	            "receiverName text not null, " +
	            "content text not null, " +
	            "createTime text not null " +
	        "); ";
    
    public static final String CREATE_TABLE_STUDENT_ROSTER = 
    		"CREATE TABLE StudentRoster " + 
            "( " + 
            	"cacheUserId text not null, " +
                "userId text not null, " + 
                "userName text not null, " + 
                "schoolId text not null, " +
                "studentNo text not null, " +
                "sex integer not null, " +
                "phone text, " +
                "picUrl text, " +
                "classId text not null, " +
                "className text not null " +
            "); ";
    
    public static final String CREATE_TABLE_PARENT_ROSTER = 
    		"CREATE TABLE ParentRoster " + 
            "( " + 
            	"cacheUserId text not null, " +
                "userId text not null, " + 
                "userName text not null, " + 
                "sex integer not null, " +
                "phone text, " +
                "picUrl text, " +
                "childUserId text not null, " +
                "childName text not null, " +
                "schoolId text not null, " +
                "classId text not null, " +
                "className text not null " +
            "); ";
	
    public static final String CREATE_TABLE_TEACHER_ROSTER = 
    		"CREATE TABLE TeacherRoster " + 
            "( " + 
            	"cacheUserId text not null, " +
                "userId text not null, " + 
                "userName text not null, " + 
                "teacherId text not null, " + 
                "sex integer text not null, " +
                "phone text, " +
                "picUrl text, " +
                "role text, " +
                "schoolId text text not null, " +
                "classId text text not null, " +
                "className text text not null " +
            "); ";
    
    public static final String CREATE_TABLE_MESSAGE = 
    		"CREATE TABLE Message " + 
            "( " + 
            	"msgId integer not null primary key autoincrement, " + 
                "msgType integer not null, " + 
                "senderId text not null, " +
                "receiverId text, " +
                "objectType integer, " + 
                "groupType integer, " + 
                "content text not null, " +
                "outFlag integer not null, " +
                "createTime text not null, " +
                "readStatus integer not null " +
            "); ";
    
    public static final String CREATE_TABLE_SMS = 
    		"CREATE TABLE SMS " + 
            "( " + 
            	"cacheUserId text not null, " +
            	"smsId integer not null primary key autoincrement, " + 
                "userList text, " + 
                "content text not null, " +
                "createTime text not null " +
            "); ";
    
    public static final String DROP_TABLE_NOTICE = "DROP TABLE IF EXISTS Notice; ";
    public static final String DROP_TABLE_HOMEWORK = "DROP TABLE IF EXISTS Homework; ";
    public static final String DROP_TABLE_DAILY = "DROP TABLE IF EXISTS Daily; ";
    public static final String DROP_TABLE_STUDENT_ROSTER = "DROP TABLE IF EXISTS StudentRoster; ";
    public static final String DROP_TABLE_PARENT_ROSTER = "DROP TABLE IF EXISTS ParentRoster; ";
    public static final String DROP_TABLE_TEACHER_ROSTER = "DROP TABLE IF EXISTS TeacherRoster; ";
    public static final String DROP_TABLE_MESSAGE = "DROP TABLE IF EXISTS Message; ";
    public static final String DROP_TABLE_SMS = "DROP TABLE IF EXISTS SMS; ";
}

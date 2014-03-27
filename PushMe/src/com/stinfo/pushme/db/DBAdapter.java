package com.stinfo.pushme.db;

import java.util.ArrayList;

import com.stinfo.pushme.MyApplication;
import com.stinfo.pushme.common.AppConstant;
import com.stinfo.pushme.common.UserCache;
import com.stinfo.pushme.entity.Daily;
import com.stinfo.pushme.entity.Group;
import com.stinfo.pushme.entity.Homework;
import com.stinfo.pushme.entity.Message;
import com.stinfo.pushme.entity.Notice;
import com.stinfo.pushme.entity.ParentRoster;
import com.stinfo.pushme.entity.SMS;
import com.stinfo.pushme.entity.StudentRoster;
import com.stinfo.pushme.entity.TeacherRoster;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBAdapter {
	private static final String TAG = "DBAdapter";
	private static final String DATABASE_NAME = "pushme.db";
	private static final int DATABASE_VERSION = 2;
	private static final int MAX_NUMBER = 50;

	private SQLiteDatabase db;
	private Context mContext;
	private DBHelper dbHelper;
	private String mUserId = "";

	public DBAdapter(Context context, String userId) {
		this.mContext = context;
		this.mUserId = userId;
		dbHelper = new DBHelper(mContext, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public DBAdapter() {
		this.mContext = MyApplication.getInstance().getApplicationContext();
		this.mUserId = UserCache.getInstance().getUserInfo().getUserId();
		dbHelper = new DBHelper(mContext, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public void open() throws SQLException {
		try {
			db = dbHelper.getWritableDatabase();
		} catch (SQLiteException ex) {
			db = dbHelper.getReadableDatabase();
		}
	}

	public void close() {
		db.close();
	}

	public void addNotice(ArrayList<Notice> list) throws SQLException {
		for (Notice notice : list) {
			ContentValues values = new ContentValues();

			values.put("cacheUserId", mUserId);
			values.put("authorId", notice.getAuthorId());
			values.put("authorName", notice.getAuthorName());
			values.put("type", notice.getType());
			values.put("objectId", notice.getObjectId());
			values.put("title", notice.getTitle());
			values.put("content", notice.getContent());
			values.put("createTime", notice.getCreateTime());
			db.insertOrThrow("Notice", null, values);
		}
	}

	private Notice fetchNotice(Cursor result) {
		Notice notice = new Notice();

		notice.setAuthorId(result.getString(result.getColumnIndex("authorId")));
		notice.setAuthorName(result.getString(result.getColumnIndex("authorName")));
		notice.setType(result.getInt(result.getColumnIndex("type")));
		notice.setObjectId(result.getString(result.getColumnIndex("objectId")));
		notice.setTitle(result.getString(result.getColumnIndex("title")));
		notice.setContent(result.getString(result.getColumnIndex("content")));
		notice.setCreateTime(result.getString(result.getColumnIndex("createTime")));

		return notice;
	}

	private ArrayList<Notice> getNoticeByWhere(String where) {
		ArrayList<Notice> list = new ArrayList<Notice>();
		String orderBy = "createTime DESC";
		String limit = String.valueOf(MAX_NUMBER);

		Cursor result = db.query("Notice", new String[] { "authorId", "authorName", "type", "objectId",
				"title", "content", "createTime" }, where, null, null, null, orderBy, limit);
		if (result.moveToFirst()) {
			do {
				list.add(fetchNotice(result));
			} while (result.moveToNext());
		}

		return list;
	}

	public ArrayList<Notice> getNotice(String objectId, int type) {
		String where = String.format("cacheUserId = '%s' AND type = %d AND objectId = '%s'", mUserId, type,
				objectId);
		return getNoticeByWhere(where);
	}

	public void addHomework(ArrayList<Homework> list) throws SQLException {
		for (Homework homework : list) {
			ContentValues values = new ContentValues();

			values.put("cacheUserId", mUserId);
			values.put("authorId", homework.getAuthorId());
			values.put("authorName", homework.getAuthorName());
			values.put("subject", homework.getSubject());
			values.put("classId", homework.getClassId());
			values.put("content", homework.getContent());
			values.put("createTime", homework.getCreateTime());
			db.insertOrThrow("Homework", null, values);
		}
	}

	private Homework fetchHomework(Cursor result) {
		Homework homework = new Homework();

		homework.setAuthorId(result.getString(result.getColumnIndex("authorId")));
		homework.setAuthorName(result.getString(result.getColumnIndex("authorName")));
		homework.setSubject(result.getString(result.getColumnIndex("subject")));
		homework.setClassId(result.getString(result.getColumnIndex("classId")));
		homework.setContent(result.getString(result.getColumnIndex("content")));
		homework.setCreateTime(result.getString(result.getColumnIndex("createTime")));

		return homework;
	}

	private ArrayList<Homework> getHomeworkByWhere(String where) {
		ArrayList<Homework> list = new ArrayList<Homework>();
		String orderBy = "createTime DESC";
		String limit = String.valueOf(MAX_NUMBER);

		Cursor result = db.query("Homework", new String[] { "authorId", "authorName", "subject", "classId",
				"content", "createTime" }, where, null, null, null, orderBy, limit);
		if (result.moveToFirst()) {
			do {
				list.add(fetchHomework(result));
			} while (result.moveToNext());
		}

		return list;
	}

	public ArrayList<Homework> getClassHomework(String classId) {
		String where = String.format("cacheUserId = '%s' AND classId = '%s'", mUserId, classId);
		return getHomeworkByWhere(where);
	}

	public ArrayList<Homework> getSubjectHomework(String classId, String subject) {
		String where = String.format("cacheUserId = '%s' AND classId = '%s' AND subject = '%s'", mUserId,
				classId, subject);
		return getHomeworkByWhere(where);
	}

	public void addDaily(ArrayList<Daily> list) throws SQLException {
		for (Daily daily : list) {
			ContentValues values = new ContentValues();

			values.put("cacheUserId", mUserId);
			values.put("authorId", daily.getAuthorId());
			values.put("authorName", daily.getAuthorName());
			values.put("receiverId", daily.getReceiverId());
			values.put("receiverName", daily.getReceiverName());
			values.put("content", daily.getContent());
			values.put("createTime", daily.getCreateTime());
			db.insertOrThrow("Daily", null, values);
		}
	}

	private Daily fetchDaily(Cursor result) {
		Daily daily = new Daily();

		daily.setAuthorId(result.getString(result.getColumnIndex("authorId")));
		daily.setAuthorName(result.getString(result.getColumnIndex("authorName")));
		daily.setReceiverId(result.getString(result.getColumnIndex("receiverId")));
		daily.setReceiverName(result.getString(result.getColumnIndex("receiverName")));
		daily.setContent(result.getString(result.getColumnIndex("content")));
		daily.setCreateTime(result.getString(result.getColumnIndex("createTime")));

		return daily;
	}

	private ArrayList<Daily> getDailyByWhere(String where) {
		ArrayList<Daily> list = new ArrayList<Daily>();
		String orderBy = "createTime DESC";
		String limit = String.valueOf(MAX_NUMBER);

		Cursor result = db.query("Daily", new String[] { "authorId", "authorName", "receiverId",
				"receiverName", "content", "createTime" }, where, null, null, null, orderBy, limit);
		if (result.moveToFirst()) {
			do {
				list.add(fetchDaily(result));
			} while (result.moveToNext());
		}

		return list;
	}

	public ArrayList<Daily> getClassDaily(String classId) {
		String where = String.format("cacheUserId = '%s' AND classId = '%s'", mUserId, classId);
		return getDailyByWhere(where);
	}

	public ArrayList<Daily> getDaily(String receiverId) {
		String where = String.format("cacheUserId = '%s' AND receiverId = '%s'", mUserId, receiverId);
		return getDailyByWhere(where);
	}

	public void addSMS(SMS sms) throws SQLException {
		ContentValues values = new ContentValues();

		values.put("cacheUserId", mUserId);
		values.put("userList", sms.getUserList());
		values.put("content", sms.getContent());
		values.put("createTime", sms.getCreateTime());
		db.insertOrThrow("SMS", null, values);
	}

	public void addSMS(ArrayList<SMS> list) throws SQLException {
		for (SMS sms : list) {
			ContentValues values = new ContentValues();

			values.put("cacheUserId", mUserId);
			values.put("userList", sms.getUserList());
			values.put("content", sms.getContent());
			values.put("createTime", sms.getCreateTime());
			db.insertOrThrow("SMS", null, values);
		}
	}

	private SMS fetchSMS(Cursor result) {
		SMS sms = new SMS();

		sms.setSmsId(result.getInt(result.getColumnIndex("smsId")));
		sms.setUserList(result.getString(result.getColumnIndex("userList")));
		sms.setContent(result.getString(result.getColumnIndex("content")));
		sms.setCreateTime(result.getString(result.getColumnIndex("createTime")));

		return sms;
	}

	public ArrayList<SMS> getOutSMS() {
		String where = String.format("cacheUserId = '%s'", mUserId);
		ArrayList<SMS> list = new ArrayList<SMS>();
		String orderBy = "createTime DESC";
		String limit = String.valueOf(MAX_NUMBER);

		Cursor result = db.query("SMS", new String[] { "smsId", "userList", "content", "createTime", },
				where, null, null, null, orderBy, limit);
		if (result.moveToFirst()) {
			do {
				list.add(fetchSMS(result));
			} while (result.moveToNext());
		}

		return list;
	}

	public void addMessage(Message message) throws SQLException {
		ContentValues values = new ContentValues();

		values.put("msgType", message.getMsgType());
		values.put("senderId", message.getSenderId());
		values.put("receiverId", message.getReceiverId());
		values.put("objectType", message.getObjectType());
		values.put("groupType", message.getGroupType());
		values.put("content", message.getContent());
		values.put("outFlag", message.getOutFlag());
		values.put("createTime", message.getCreateTime());
		values.put("readStatus", message.getReadStatus());
		db.insertOrThrow("Message", null, values);
	}

	private Message fetchMessage(Cursor result) {
		Message message = new Message();

		message.setMsgType(result.getInt(result.getColumnIndex("msgType")));
		message.setSenderId(result.getString(result.getColumnIndex("senderId")));
		message.setReceiverId(result.getString(result.getColumnIndex("receiverId")));
		message.setObjectType(result.getInt(result.getColumnIndex("objectType")));
		message.setGroupType(result.getInt(result.getColumnIndex("groupType")));
		message.setContent(result.getString(result.getColumnIndex("content")));
		message.setOutFlag(result.getInt(result.getColumnIndex("outFlag")));
		message.setCreateTime(result.getString(result.getColumnIndex("createTime")));
		message.setReadStatus(result.getInt(result.getColumnIndex("readStatus")));
		message.setSendStatus(AppConstant.MessageSendStatus.SUCCESS);
		return message;
	}

	private ArrayList<Message> getMessageByWhere(String where) {
		ArrayList<Message> list = new ArrayList<Message>();
		String orderBy = "createTime DESC";
		String limit = String.valueOf(MAX_NUMBER);

		Cursor result = db.query("Message", new String[] { "msgType", "senderId", "receiverId", "objectType",
				"groupType", "content", "outFlag", "createTime", "readStatus" }, where, null, null, null,
				orderBy, limit);
		if (result.moveToFirst()) {
			do {
				list.add(fetchMessage(result));
			} while (result.moveToNext());
		}

		return list;
	}

	public ArrayList<Message> getPersonalMessage(String otherId) {
		String where = String
				.format("(senderId = '%s' AND outFlag = 0) OR (senderId = '%s' AND receiverId = '%s' AND outFlag = 1)",
						otherId, mUserId, otherId);
		return getMessageByWhere(where);
	}

	public ArrayList<Message> getGroupMessage(Group group) {
		String where = String
				.format("(senderId <> '%s' AND outFlag = 0) AND (receiverId = '%s' AND objectType = %d AND groupType = %d)",
						mUserId, group.getGroupId(), group.getObjectType(), group.getGroupType());
		return getMessageByWhere(where);
	}

	public void deleteMyRoster() {
		String where = String.format("cacheUserId = '%s'", mUserId);
		db.delete("StudentRoster", where, null);
		db.delete("TeacherRoster", where, null);
		db.delete("ParentRoster", where, null);
	}

	public void addStudentRoster(ArrayList<StudentRoster> list) throws SQLException {
		for (StudentRoster student : list) {
			ContentValues values = new ContentValues();

			values.put("cacheUserId", mUserId);
			values.put("userId", student.getUserId());
			values.put("userName", student.getUserName());
			values.put("schoolId", student.getSchoolId());
			values.put("studentNo", student.getStudentNo());
			values.put("sex", student.getSex());
			values.put("phone", student.getPhone());
			values.put("picUrl", student.getPicUrl());
			values.put("classId", student.getClassId());
			values.put("className", student.getClassName());
			db.insertOrThrow("StudentRoster", null, values);
		}
	}

	private StudentRoster fetchStudentRoster(Cursor result) {
		StudentRoster student = new StudentRoster();

		student.setUserId(result.getString(result.getColumnIndex("userId")));
		student.setUserName(result.getString(result.getColumnIndex("userName")));
		student.setSchoolId(result.getString(result.getColumnIndex("schoolId")));
		student.setStudentNo(result.getString(result.getColumnIndex("studentNo")));
		student.setSex(result.getInt(result.getColumnIndex("sex")));
		student.setPhone(result.getString(result.getColumnIndex("phone")));
		student.setPicUrl(result.getString(result.getColumnIndex("picUrl")));
		student.setClassId(result.getString(result.getColumnIndex("classId")));
		student.setClassName(result.getString(result.getColumnIndex("className")));

		return student;
	}

	private ArrayList<StudentRoster> getStudentRosterByWhere(String where) {
		ArrayList<StudentRoster> list = new ArrayList<StudentRoster>();
		String limit = String.valueOf(MAX_NUMBER);

		Cursor result = db.query("StudentRoster", new String[] { "userId", "userName", "schoolId",
				"studentNo", "sex", "phone", "picUrl", "classId", "className" }, where, null, null, null,
				null, limit);
		if (result.moveToFirst()) {
			do {
				list.add(fetchStudentRoster(result));
			} while (result.moveToNext());
		}

		return list;
	}

	public ArrayList<StudentRoster> getClassStudentRoster(String classId) {
		String where = String.format("cacheUserId = '%s' AND classId = '%s'", mUserId, classId);
		return getStudentRosterByWhere(where);
	}

	public void addParentRoster(ArrayList<ParentRoster> list) throws SQLException {
		for (ParentRoster parent : list) {
			ContentValues values = new ContentValues();

			values.put("cacheUserId", mUserId);
			values.put("userId", parent.getUserId());
			values.put("userName", parent.getUserName());
			values.put("schoolId", parent.getSchoolId());
			values.put("sex", parent.getSex());
			values.put("phone", parent.getPhone());
			values.put("picUrl", parent.getPicUrl());
			values.put("classId", parent.getClassId());
			values.put("className", parent.getClassName());
			values.put("childUserId", parent.getChildUserId());
			values.put("childName", parent.getChildName());
			db.insertOrThrow("ParentRoster", null, values);
		}
	}

	private ParentRoster fetchParentRoster(Cursor result) {
		ParentRoster parent = new ParentRoster();

		parent.setUserId(result.getString(result.getColumnIndex("userId")));
		parent.setUserName(result.getString(result.getColumnIndex("userName")));
		parent.setSchoolId(result.getString(result.getColumnIndex("schoolId")));
		parent.setSex(result.getInt(result.getColumnIndex("sex")));
		parent.setPhone(result.getString(result.getColumnIndex("phone")));
		parent.setPicUrl(result.getString(result.getColumnIndex("picUrl")));
		parent.setClassId(result.getString(result.getColumnIndex("classId")));
		parent.setClassName(result.getString(result.getColumnIndex("className")));
		parent.setChildUserId(result.getString(result.getColumnIndex("childUserId")));
		parent.setChildName(result.getString(result.getColumnIndex("childName")));

		return parent;
	}

	private ArrayList<ParentRoster> getParentRosterByWhere(String where) {
		ArrayList<ParentRoster> list = new ArrayList<ParentRoster>();
		String limit = String.valueOf(MAX_NUMBER);

		Cursor result = db.query("ParentRoster", new String[] { "userId", "userName", "schoolId", "sex",
				"phone", "picUrl", "classId", "className", "childUserId", "childName" }, where, null, null,
				null, null, limit);
		if (result.moveToFirst()) {
			do {
				list.add(fetchParentRoster(result));
			} while (result.moveToNext());
		}

		return list;
	}

	public ArrayList<ParentRoster> getClassParentRoster(String classId) {
		String where = String.format("cacheUserId = '%s' AND classId = '%s'", mUserId, classId);
		return getParentRosterByWhere(where);
	}

	public void addTeacherRoster(ArrayList<TeacherRoster> list) throws SQLException {
		for (TeacherRoster teacher : list) {
			ContentValues values = new ContentValues();

			values.put("cacheUserId", mUserId);
			values.put("userId", teacher.getUserId());
			values.put("userName", teacher.getUserName());
			values.put("schoolId", teacher.getSchoolId());
			values.put("teacherId", teacher.getTeacherId());
			values.put("sex", teacher.getSex());
			values.put("phone", teacher.getPhone());
			values.put("picUrl", teacher.getPicUrl());
			values.put("role", teacher.getRole());
			values.put("classId", teacher.getClassId());
			values.put("className", teacher.getClassName());
			db.insertOrThrow("TeacherRoster", null, values);
		}
	}

	private TeacherRoster fetchTeacherRoster(Cursor result) {
		TeacherRoster teacher = new TeacherRoster();

		teacher.setUserId(result.getString(result.getColumnIndex("userId")));
		teacher.setUserName(result.getString(result.getColumnIndex("userName")));
		teacher.setSchoolId(result.getString(result.getColumnIndex("schoolId")));
		teacher.setTeacherId(result.getString(result.getColumnIndex("teacherId")));
		teacher.setSex(result.getInt(result.getColumnIndex("sex")));
		teacher.setPhone(result.getString(result.getColumnIndex("phone")));
		teacher.setPicUrl(result.getString(result.getColumnIndex("picUrl")));
		teacher.setRole(result.getString(result.getColumnIndex("role")));
		teacher.setClassId(result.getString(result.getColumnIndex("classId")));
		teacher.setClassName(result.getString(result.getColumnIndex("className")));

		return teacher;
	}

	private ArrayList<TeacherRoster> getTeacherRosterByWhere(String where) {
		ArrayList<TeacherRoster> list = new ArrayList<TeacherRoster>();
		String limit = String.valueOf(MAX_NUMBER);

		Cursor result = db.query("TeacherRoster", new String[] { "userId", "userName", "schoolId",
				"teacherId", "sex", "phone", "picUrl", "role", "classId", "className" }, where, null, null,
				null, null, limit);
		if (result.moveToFirst()) {
			do {
				list.add(fetchTeacherRoster(result));
			} while (result.moveToNext());
		}

		return list;
	}

	public ArrayList<TeacherRoster> getClassTeacherRoster(String classId) {
		String where = String.format("cacheUserId = '%s' AND classId = '%s'", mUserId, classId);
		return getTeacherRosterByWhere(where);
	}

	public ArrayList<TeacherRoster> getSchoolTeacherRoster(String schoolId) {
		String where = String.format("cacheUserId = '%s' AND schoolId = '%s'", mUserId, schoolId);
		return getTeacherRosterByWhere(where);
	}

	private static class DBHelper extends SQLiteOpenHelper {

		public DBHelper(Context mContext, String name, CursorFactory factory, int version) {
			super(mContext, name, factory, version);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(DBScript.CREATE_TABLE_NOTICE);
			db.execSQL(DBScript.CREATE_TABLE_HOMEWORK);
			db.execSQL(DBScript.CREATE_TABLE_DAILY);
			db.execSQL(DBScript.CREATE_TABLE_STUDENT_ROSTER);
			db.execSQL(DBScript.CREATE_TABLE_PARENT_ROSTER);
			db.execSQL(DBScript.CREATE_TABLE_TEACHER_ROSTER);
			db.execSQL(DBScript.CREATE_TABLE_MESSAGE);
			db.execSQL(DBScript.CREATE_TABLE_SMS);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(TAG, "Upgrading from version " + oldVersion + " to " + newVersion
					+ ", which will destroy all old data");

			db.execSQL(DBScript.DROP_TABLE_NOTICE);
			db.execSQL(DBScript.DROP_TABLE_HOMEWORK);
			db.execSQL(DBScript.DROP_TABLE_DAILY);
			db.execSQL(DBScript.DROP_TABLE_STUDENT_ROSTER);
			db.execSQL(DBScript.DROP_TABLE_PARENT_ROSTER);
			db.execSQL(DBScript.DROP_TABLE_TEACHER_ROSTER);
			db.execSQL(DBScript.DROP_TABLE_MESSAGE);
			db.execSQL(DBScript.DROP_TABLE_SMS);
			onCreate(db);
		}
	}
}
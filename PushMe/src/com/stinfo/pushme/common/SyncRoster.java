package com.stinfo.pushme.common;

import java.util.ArrayList;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;

import com.stinfo.pushme.db.DBAdapter;
import com.stinfo.pushme.entity.ClassInfo;
import com.stinfo.pushme.entity.ParentRoster;
import com.stinfo.pushme.entity.StudentRoster;
import com.stinfo.pushme.entity.Teacher;
import com.stinfo.pushme.entity.TeacherRoster;
import com.stinfo.pushme.rest.MyStringRequest;
import com.stinfo.pushme.rest.RequestController;
import com.stinfo.pushme.rest.entity.ParentListReq;
import com.stinfo.pushme.rest.entity.ParentListResp;
import com.stinfo.pushme.rest.entity.SchoolTeacherListReq;
import com.stinfo.pushme.rest.entity.SchoolTeacherListResp;
import com.stinfo.pushme.rest.entity.StudentListReq;
import com.stinfo.pushme.rest.entity.StudentListResp;
import com.stinfo.pushme.rest.entity.TeacherListReq;
import com.stinfo.pushme.rest.entity.TeacherListResp;

public class SyncRoster {
	private static final String TAG = "SyncMyRoster";
	private OnTaskListener taskListener = null;

	public interface OnTaskListener {
		public void onSuccess();

		public void onFailure();
	}

	public SyncRoster(OnTaskListener taskListener) {
		this.taskListener = taskListener;
	}

	public void execSync() {
		DBAdapter dbAdapter = new DBAdapter();
		try {
			dbAdapter.open();
			dbAdapter.deleteMyRoster();
		} catch (Exception e) {
			Log.e(TAG, "Failed to operate database: " + e);
			taskListener.onFailure();
			return;
		} finally {
			dbAdapter.close();
		}

		step1_syncStudent();
	}

	private void step1_syncStudent() {
		UserCache userCache = UserCache.getInstance();
		StudentListReq reqData = new StudentListReq();
		reqData.setClassId(userCache.getCurrentClass().getClassId());
		reqData.setUserId(userCache.getUserInfo().getUserId());
		reqData.setSessionKey(userCache.getSessionKey());

		MyStringRequest req = new MyStringRequest(Method.GET, reqData.getAllUrl(),
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						Log.d(TAG, "Response: " + response);
						try {
							UserCache userCache = UserCache.getInstance();
							ClassInfo classInfo = userCache.getCurrentClass();
							StudentListResp resp = new StudentListResp(response, classInfo);
							if ((resp.getAck() != AppConstant.Ack.SUCCESS)
									&& resp.getAck() != AppConstant.Ack.NOT_FOUND) {
								taskListener.onFailure();
							}
							if (resp.getAck() == AppConstant.Ack.SUCCESS) {
								updateStudent(resp.getStudentList());
							}

							step2_syncParent();
						} catch (Exception e) {
							taskListener.onFailure();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						taskListener.onFailure();
					}
				});

		RequestController.getInstance().addToRequestQueue(req, TAG);
	}

	private void step2_syncParent() {
		UserCache userCache = UserCache.getInstance();
		ParentListReq reqData = new ParentListReq();
		reqData.setClassId(userCache.getCurrentClass().getClassId());
		reqData.setUserId(userCache.getUserInfo().getUserId());
		reqData.setSessionKey(userCache.getSessionKey());

		MyStringRequest req = new MyStringRequest(Method.GET, reqData.getAllUrl(),
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						Log.d(TAG, "Response: " + response);
						try {
							UserCache userCache = UserCache.getInstance();
							ClassInfo classInfo = userCache.getCurrentClass();

							ParentListResp resp = new ParentListResp(response, classInfo);
							if ((resp.getAck() != AppConstant.Ack.SUCCESS)
									&& resp.getAck() != AppConstant.Ack.NOT_FOUND) {
								taskListener.onFailure();
							}
							if (resp.getAck() == AppConstant.Ack.SUCCESS) {
								updateParent(resp.getParentList());
							}

							if (userCache.getUserInfo() instanceof Teacher) {
								step3_syncSchoolTeacher();
							} else {
								step3_syncClassTeacher();
							}
						} catch (Exception e) {
							taskListener.onFailure();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						taskListener.onFailure();
					}
				});

		RequestController.getInstance().addToRequestQueue(req, TAG);
	}

	private void step3_syncClassTeacher() {
		UserCache userCache = UserCache.getInstance();
		TeacherListReq reqData = new TeacherListReq();
		reqData.setClassId(userCache.getCurrentClass().getClassId());
		reqData.setUserId(userCache.getUserInfo().getUserId());
		reqData.setSessionKey(userCache.getSessionKey());

		MyStringRequest req = new MyStringRequest(Method.GET, reqData.getAllUrl(),
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						Log.d(TAG, "Response: " + response);
						try {
							UserCache userCache = UserCache.getInstance();
							ClassInfo classInfo = userCache.getCurrentClass();
							TeacherListResp resp = new TeacherListResp(response, classInfo);
							if ((resp.getAck() != AppConstant.Ack.SUCCESS)
									&& resp.getAck() != AppConstant.Ack.NOT_FOUND) {
								taskListener.onFailure();
							}
							if (resp.getAck() == AppConstant.Ack.SUCCESS) {
								updateTeacher(resp.getTeacherList());
							}

							taskListener.onSuccess(); // SUCESS
						} catch (Exception e) {
							taskListener.onFailure();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						taskListener.onFailure();
					}
				});

		RequestController.getInstance().addToRequestQueue(req, TAG);
	}

	private void step3_syncSchoolTeacher() {
		UserCache userCache = UserCache.getInstance();
		SchoolTeacherListReq reqData = new SchoolTeacherListReq();
		reqData.setSchoolId(userCache.getCurrentClass().getSchoolId());
		reqData.setUserId(userCache.getUserInfo().getUserId());
		reqData.setSessionKey(userCache.getSessionKey());

		MyStringRequest req = new MyStringRequest(Method.GET, reqData.getAllUrl(),
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						Log.d(TAG, "Response: " + response);
						try {
							UserCache userCache = UserCache.getInstance();
							ClassInfo classInfo = userCache.getCurrentClass();
							SchoolTeacherListResp resp = new SchoolTeacherListResp(response,
									classInfo.getSchoolId());
							if ((resp.getAck() != AppConstant.Ack.SUCCESS)
									&& resp.getAck() != AppConstant.Ack.NOT_FOUND) {
								taskListener.onFailure();
							}
							if (resp.getAck() == AppConstant.Ack.SUCCESS) {
								updateTeacher(resp.getTeacherList());
							}

							taskListener.onSuccess(); // SUCESS
						} catch (Exception e) {
							taskListener.onFailure();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						taskListener.onFailure();
					}
				});

		RequestController.getInstance().addToRequestQueue(req, TAG);
	}

	private void updateStudent(ArrayList<StudentRoster> list) {
		Log.d("[SyncRoster] updateStudent: ", String.valueOf(list.size()));
		for (StudentRoster roster: list) {
			Log.d("[user: ]", roster.getUserName());
		}
		
		DBAdapter dbAdapter = new DBAdapter();
		try {
			dbAdapter.open();
			dbAdapter.addStudentRoster(list);
		} finally {
			dbAdapter.close();
		}
	}

	private void updateParent(ArrayList<ParentRoster> list) {
		Log.d("[SyncRoster] updateParent: ", String.valueOf(list.size()));
		for (ParentRoster roster: list) {
			Log.d("[user: ]", roster.getUserName());
		}
		
		DBAdapter dbAdapter = new DBAdapter();
		try {
			dbAdapter.open();
			dbAdapter.addParentRoster(list);
		} finally {
			dbAdapter.close();
		}
	}

	private void updateTeacher(ArrayList<TeacherRoster> list) {
		Log.d("[SyncRoster] updateTeacher: ", String.valueOf(list.size()));
		for (TeacherRoster roster: list) {
			Log.d("[user: ]", roster.getUserName());
		}
		
		DBAdapter dbAdapter = new DBAdapter();
		try {
			dbAdapter.open();
			dbAdapter.addTeacherRoster(list);
		} finally {
			dbAdapter.close();
		}
	}
}

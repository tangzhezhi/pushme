package com.stinfo.pushme.fragment;

import com.stinfo.pushme.R;
import com.stinfo.pushme.activity.DailyActivity;
import com.stinfo.pushme.activity.HomeworkActivity;
import com.stinfo.pushme.activity.NoticeActivity;

import android.content.Intent;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

public class IndexFragment extends Fragment implements OnClickListener {
	private RelativeLayout layoutNotice;
	private RelativeLayout layoutHomeWork;
	private RelativeLayout layoutDaily;
	private RelativeLayout layoutTimetable;
	private RelativeLayout layoutScore;
	private RelativeLayout layoutAttendance;
	private View mView;

	public static IndexFragment newInstance() {
		IndexFragment newFragment = new IndexFragment();
		return newFragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.fragment_index, container, false);
		initView();
		return mView;
	}

	private void initView() {
		layoutNotice = (RelativeLayout) mView.findViewById(R.id.layout_notice);
		layoutHomeWork = (RelativeLayout) mView.findViewById(R.id.layout_homework);
		layoutDaily = (RelativeLayout) mView.findViewById(R.id.layout_daily);
		layoutTimetable = (RelativeLayout) mView.findViewById(R.id.layout_timetable);
		layoutScore = (RelativeLayout) mView.findViewById(R.id.layout_score);
		layoutAttendance = (RelativeLayout) mView.findViewById(R.id.layout_attendance);

		layoutNotice.setOnClickListener(this);
		layoutHomeWork.setOnClickListener(this);
		layoutDaily.setOnClickListener(this);
		layoutTimetable.setOnClickListener(this);
		layoutScore.setOnClickListener(this);
		layoutAttendance.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_timetable:
			onTimetableClick();
			break;
		case R.id.layout_notice:
			onNoticeClick();
			break;
		case R.id.layout_daily:
			onDailyClick();
			break;
		case R.id.layout_score:
			onScoreClick();
			break;
		case R.id.layout_attendance:
			onAttendanceClick();
			break;
		case R.id.layout_homework:
			onHomeWorkClick();
			break;
		}
	}

	private void onNoticeClick() {
		Intent intent = new Intent(getActivity(), NoticeActivity.class);
		getActivity().startActivity(intent);
	}

	private void onDailyClick() {
		Intent intent = new Intent(getActivity(), DailyActivity.class);
		getActivity().startActivity(intent);
	}

	private void onHomeWorkClick() {
		Intent intent = new Intent(getActivity(), HomeworkActivity.class);
		getActivity().startActivity(intent);
	}

	private void onTimetableClick() {

	}

	private void onScoreClick() {

	}

	private void onAttendanceClick() {

	}

}

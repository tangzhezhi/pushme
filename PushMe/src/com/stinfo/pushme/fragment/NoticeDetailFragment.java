package com.stinfo.pushme.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.stinfo.pushme.R;
import com.stinfo.pushme.entity.Notice;
import com.stinfo.pushme.util.DateTimeUtil;

public final class NoticeDetailFragment extends Fragment {
	private Notice mNotice = null;

	public static NoticeDetailFragment newInstance(Notice notice) {
		NoticeDetailFragment newFragment = new NoticeDetailFragment();
		Bundle bundle = new Bundle();

		bundle.putSerializable("notice", notice);
		newFragment.setArguments(bundle);
		return newFragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle bundle = getArguments();
		if (bundle != null) {
			mNotice = (Notice) bundle.getSerializable("notice");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_notice_detail, container, false);

		TextView tvAuthor = (TextView) view.findViewById(R.id.tv_author);
		TextView tvPublishTime = (TextView) view.findViewById(R.id.tv_publish_time);
		TextView tvTitle = (TextView) view.findViewById(R.id.tv_title);
		TextView tvContent = (TextView) view.findViewById(R.id.tv_content);

		if (mNotice != null) {
			tvAuthor.setText(mNotice.getAuthorName());
			tvPublishTime.setText(DateTimeUtil.toStandardTime(mNotice.getCreateTime()));
			tvTitle.setText(mNotice.getTitle());
			tvContent.setText(mNotice.getContent());
		}
		return view;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}
}

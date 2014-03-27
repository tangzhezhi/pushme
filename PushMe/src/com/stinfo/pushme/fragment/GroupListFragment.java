package com.stinfo.pushme.fragment;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.stinfo.pushme.R;
import com.stinfo.pushme.activity.GroupChatActivity;
import com.stinfo.pushme.adapter.GroupListAdapter;
import com.stinfo.pushme.common.AppConstant;
import com.stinfo.pushme.common.UserCache;
import com.stinfo.pushme.entity.ClassInfo;
import com.stinfo.pushme.entity.Group;

public final class GroupListFragment extends Fragment implements OnItemClickListener {
	private ArrayList<Group> mGroupList = null;
	private GroupListAdapter mAdapter;
	private ListView lvGroupList;
	private View mView;

	public static GroupListFragment newInstance() {
		GroupListFragment newFragment = new GroupListFragment();
		return newFragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.fragment_group_list, container, false);
		initData();
		return mView;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	private void initData() {
		initGroup();
		lvGroupList = (ListView) mView.findViewById(R.id.lv_group_list);
		mAdapter = new GroupListAdapter(mView.getContext(), mGroupList);
		lvGroupList.setAdapter(mAdapter);
		lvGroupList.setOnItemClickListener(this);
	}

	private void initGroup() {
		mGroupList = new ArrayList<Group>();

		ClassInfo classInfo = UserCache.getInstance().getCurrentClass();
		String groupId = classInfo.getClassId();
		Group group1 = new Group("老师群", groupId, AppConstant.MessageObjectType.TEACHER,
				AppConstant.MessageGroupType.CLASS);
		Group group2 = new Group("学生群", groupId, AppConstant.MessageObjectType.STUDENT,
				AppConstant.MessageGroupType.CLASS);
		Group group3 = new Group("家长群", groupId, AppConstant.MessageObjectType.PARENT,
				AppConstant.MessageGroupType.CLASS);

		mGroupList.add(group1);
		mGroupList.add(group2);
		mGroupList.add(group3);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View v, int pos, long id) {
		switch (parent.getId()) {
		case R.id.lv_group_list:
			Intent intent = new Intent(getActivity(), GroupChatActivity.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable("group", mGroupList.get(pos));
			intent.putExtras(bundle);
			startActivity(intent);
			break;
		}
	}
}

package com.stinfo.pushme.adapter;

import java.util.ArrayList;

import com.stinfo.pushme.entity.Notice;
import com.stinfo.pushme.fragment.NoticeDetailFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public final class NoticeDetailAdapter extends FragmentStatePagerAdapter {
	private ArrayList<Notice> mNoticeList;

	public NoticeDetailAdapter(FragmentManager fm, ArrayList<Notice> noticeList) {
		super(fm);
		mNoticeList = noticeList;
	}
	
	@Override
	public int getCount() {
		return mNoticeList.size();
	}

	@Override
	public Fragment getItem(int position) {
		return NoticeDetailFragment.newInstance(mNoticeList.get(position));
	}
	
	@Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }
}
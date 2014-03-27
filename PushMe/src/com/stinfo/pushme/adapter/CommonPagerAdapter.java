package com.stinfo.pushme.adapter;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public final class CommonPagerAdapter extends FragmentPagerAdapter {
	private ArrayList<Fragment> mFragmentsList;

	public CommonPagerAdapter(FragmentManager fm, ArrayList<Fragment> fragmentsList) {
		super(fm);
		mFragmentsList = fragmentsList;
	}
	
	@Override
	public int getCount() {
		return mFragmentsList.size();
	}

	@Override
	public Fragment getItem(int position) {
		return mFragmentsList.get(position);
	}
	
	@Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }
}
package com.stinfo.pushme.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.stinfo.pushme.BaseActionBarActivity;
import com.stinfo.pushme.R;
import com.stinfo.pushme.adapter.TabsAdapter;
import com.stinfo.pushme.common.UserCache;
import com.stinfo.pushme.entity.Teacher;
import com.stinfo.pushme.fragment.NoticeClassFragment;
import com.stinfo.pushme.fragment.NoticeSchoolFragment;

public class NoticeActivity extends BaseActionBarActivity {
	TabsAdapter mTabsAdapter;
	ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mViewPager = new ViewPager(this);
		mViewPager.setId(11);
		setContentView(mViewPager);
		
		ActionBar bar = getSupportActionBar();
		bar.setTitle(getResources().getString(R.string.notice));
		bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		bar.setDisplayHomeAsUpEnabled(true);

		mTabsAdapter = new TabsAdapter(this, mViewPager);
		mTabsAdapter.addTab(bar.newTab().setText("班级公告"), NoticeClassFragment.class, null);
		mTabsAdapter.addTab(bar.newTab().setText("学校公告"), NoticeSchoolFragment.class, null);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.notice, menu);
		if (!(UserCache.getInstance().getUserInfo() instanceof Teacher)) {
			MenuItem editNotice = menu.findItem(R.id.action_edit);
			editNotice.setVisible(false);
		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_edit:
			onEditNotice();
			break;
		case android.R.id.home:
			this.finish();
			break;
		}
		return true;
	}

	public void onEditNotice() {
		Intent intent = new Intent(NoticeActivity.this, PostNoticeActivity.class);
		NoticeActivity.this.startActivity(intent);
	}
}
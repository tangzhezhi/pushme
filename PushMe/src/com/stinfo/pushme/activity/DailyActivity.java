package com.stinfo.pushme.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import com.stinfo.pushme.BaseActionBarActivity;
import com.stinfo.pushme.R;

public class DailyActivity extends BaseActionBarActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_daily);
		
		ActionBar bar = getSupportActionBar();
		bar.setTitle(getResources().getString(R.string.daily));
		bar.setDisplayHomeAsUpEnabled(true);
		initView();
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			this.finish();
			break;
		}
		return true;
	}

	private void initView() {
	}
}

package com.stinfo.pushme.activity;

import com.stinfo.pushme.R;
import com.stinfo.pushme.BaseActionBarActivity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

public class AboutActivity extends BaseActionBarActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		
		ActionBar bar = getSupportActionBar();
		bar.setTitle(getResources().getString(R.string.about_me));
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

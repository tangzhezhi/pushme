package com.stinfo.pushme.activity;

import com.stinfo.pushme.R;
import com.stinfo.pushme.BaseActionBarActivity;
import com.stinfo.pushme.entity.Group;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

public class GroupChatActivity extends BaseActionBarActivity {
	private Group mGroup = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat);
		mGroup = (Group) getIntent().getSerializableExtra("group");

		ActionBar bar = getSupportActionBar();
		bar.setTitle(mGroup.getGroupName());
		bar.setDisplayHomeAsUpEnabled(true);
		initView();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			this.finish();
			Intent intent = new Intent(this, MainActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
			break;
		}
		return true;
	}

	private void initView() {
	}
}

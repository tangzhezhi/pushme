package com.stinfo.pushme.fragment;

import com.stinfo.pushme.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MessageFragment extends Fragment {
	private View mView;
	
	public static MessageFragment newInstance() {
		MessageFragment newFragment = new MessageFragment();
		return newFragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.fragment_message, container, false);
		initView();
		return mView;
	}
	
	private void initView() {
		
	}
}

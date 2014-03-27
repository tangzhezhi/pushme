package com.stinfo.pushme.fragment;

import com.stinfo.pushme.R;
import com.stinfo.pushme.util.FragmentSwitcher;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class ContactFragment extends Fragment implements SearchView.OnQueryTextListener {
	private TeacherListFragment mTeacherFrag;
	private StudentListFragment mStudentFrag;
	private ParentListFragment mParentFrag;
	private GroupListFragment mGroupFrag;
	private Fragment mCurrentFrag;
	private RadioGroup mRadioGroup;
	private MenuItem mSearchItem;
	private View mView;

	public static ContactFragment newInstance() {
		ContactFragment newFragment = new ContactFragment();
		return newFragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.fragment_contact, container, false);
		initView();
		return mView;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.contact, menu);
		mSearchItem = menu.findItem(R.id.action_search);
		SearchView searchView = (SearchView) MenuItemCompat.getActionView(mSearchItem);
		searchView.setOnQueryTextListener(this);
	}

	private void initView() {
		mTeacherFrag = TeacherListFragment.newInstance();
		mStudentFrag = StudentListFragment.newInstance();
		mParentFrag = ParentListFragment.newInstance();
		mGroupFrag = GroupListFragment.newInstance();

		mRadioGroup = (RadioGroup) mView.findViewById(R.id.group_contact);
		mRadioGroup.setOnCheckedChangeListener(listener);

		getChildFragmentManager().beginTransaction().add(R.id.fragment_container, mTeacherFrag).commit();
		mCurrentFrag = mTeacherFrag;
	}

	private OnCheckedChangeListener listener = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			switch (group.getCheckedRadioButtonId()) {
			case R.id.rb_teacher:
				mSearchItem.setVisible(true);
				FragmentSwitcher.switchFragment(getChildFragmentManager(), mCurrentFrag, mTeacherFrag);
				mCurrentFrag = mTeacherFrag;
				break;
			case R.id.rb_student:
				mSearchItem.setVisible(true);
				FragmentSwitcher.switchFragment(getChildFragmentManager(), mCurrentFrag, mStudentFrag);
				mCurrentFrag = mStudentFrag;
				break;
			case R.id.rb_parent:
				mSearchItem.setVisible(true);
				FragmentSwitcher.switchFragment(getChildFragmentManager(), mCurrentFrag, mParentFrag);
				mCurrentFrag = mParentFrag;
				break;
			case R.id.rb_group:
				mSearchItem.setVisible(false);
				FragmentSwitcher.switchFragment(getChildFragmentManager(), mCurrentFrag, mGroupFrag);
				mCurrentFrag = mGroupFrag;
				break;
			}
		}
	};

	@Override
	public boolean onQueryTextChange(String query) {
		if (mCurrentFrag instanceof TeacherListFragment) {
			mTeacherFrag.onUserFilter(query);
		} else if (mCurrentFrag instanceof StudentListFragment) {
			mStudentFrag.onUserFilter(query);
		} else if (mCurrentFrag instanceof ParentListFragment) {
			mParentFrag.onUserFilter(query);
		}
		return false;
	}

	@Override
	public boolean onQueryTextSubmit(String arg0) {
		// TODO Auto-generated method stub
		return false;
	}
}
package com.stinfo.pushme.adapter;

import java.util.ArrayList;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.stinfo.pushme.MyBaseAdatper;
import com.stinfo.pushme.R;

import com.stinfo.pushme.entity.StudentRoster;
import com.stinfo.pushme.entity.UserHelper;
import com.stinfo.pushme.entity.UserInfo;
import com.stinfo.pushme.rest.ImageCacheManager;
import com.stinfo.pushme.util.StringMatcher;
import com.stinfo.pushme.util.URLChecker;
import com.stinfo.pushme.view.RoundedImageView;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.SectionIndexer;
import android.widget.TextView;

public class UserListAdapter extends MyBaseAdatper implements Filterable, SectionIndexer {
	private static final String TAG = "UserListAdapter";
	private String mSections = "#ABCDEFGHIJKLMNOPQRSTUVWXYZ";

	private LayoutInflater mInflater;
	private ArrayList<UserInfo> mFilterList;
	private ArrayList<UserInfo> mOriginalList;
	private UserFilter mUserFilter = null;

	public UserListAdapter(Context context, ArrayList<UserInfo> list) {
		super();
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mOriginalList = new ArrayList<UserInfo>(list);
		mFilterList = list;
	}

	public void updateListData(ArrayList<UserInfo> list) {
		mOriginalList = new ArrayList<UserInfo>(list);
		mFilterList = list;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mFilterList.size();
	}

	@Override
	public Object getItem(int pos) {
		return mFilterList.get(pos);
	}

	@Override
	public long getItemId(int pos) {
		return pos;
	}

	@Override
	public View getView(int pos, View convertView, ViewGroup parentView) {
		ViewHolder holder = null;

		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_user, null);

			holder = new ViewHolder();
			holder.ivUserAvatar = (RoundedImageView) convertView.findViewById(R.id.iv_user_avatar);
			holder.tvUserName = (TextView) convertView.findViewById(R.id.tv_user_name);
			holder.tvRemark = (TextView) convertView.findViewById(R.id.tv_remark);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		UserInfo userInfo = mFilterList.get(pos);
		if (userInfo != null) {
			try {
				if (URLChecker.isUrl(userInfo.getPicUrl())) {
					Log.d(TAG, "loadImage: " + userInfo.getPicUrl());
					ImageListener listener = ImageLoader.getImageListener(holder.ivUserAvatar,
							UserHelper.getDefaultImage(userInfo), UserHelper.getErrorImage(userInfo));
					ImageCacheManager.getInstance().getImageLoader().get(userInfo.getPicUrl(), listener);
				}
			} catch (Exception e) {
				Log.e(TAG, "Failed to loadImage: " + userInfo.getPicUrl());
			}

			holder.tvUserName.setText(userInfo.getUserName());
			if (userInfo instanceof StudentRoster) {
				holder.tvRemark.setVisibility(View.GONE);
			} else {
				holder.tvRemark.setText(UserHelper.getRemark(userInfo));
			}
		}

		return convertView;
	}

	private final class ViewHolder {
		public RoundedImageView ivUserAvatar;
		public TextView tvUserName;
		public TextView tvRemark;
	}

	@Override
	public Filter getFilter() {
		if (mUserFilter == null) {
			mUserFilter = new UserFilter(mOriginalList, mFilterList, this);
		}
		return mUserFilter;
	}

	@Override
	public int getPositionForSection(int section) {
		for (int i = section; i >= 0; i--) {
			for (int j = 0; j < getCount(); j++) {
				UserInfo userInfo = (UserInfo) getItem(j);
				if (i == 0) {
					for (int k = 0; k <= 9; k++) {
						if (StringMatcher.match(userInfo.getInitial(), String.valueOf(k)))
							return j;
					}
				} else {
					if (StringMatcher.match(userInfo.getInitial(), String.valueOf(mSections.charAt(i))))
						return j;
				}
			}
		}
		return 0;
	}

	@Override
	public int getSectionForPosition(int position) {
		return 0;
	}

	@Override
	public Object[] getSections() {
		String[] sections = new String[mSections.length()];
		for (int i = 0; i < mSections.length(); i++)
			sections[i] = String.valueOf(mSections.charAt(i));
		return sections;
	}
}
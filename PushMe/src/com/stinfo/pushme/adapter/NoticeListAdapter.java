package com.stinfo.pushme.adapter;

import java.util.ArrayList;

import com.stinfo.pushme.MyBaseAdatper;
import com.stinfo.pushme.R;
import com.stinfo.pushme.entity.Notice;
import com.stinfo.pushme.util.DateTimeUtil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class NoticeListAdapter extends MyBaseAdatper {

	private LayoutInflater mInflater;
	private ArrayList<Notice> mNoticeList;

	public NoticeListAdapter(Context context, ArrayList<Notice> noticeList) {
		super();
		mNoticeList = noticeList;
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return mNoticeList.size();
	}

	@Override
	public Object getItem(int pos) {
		return mNoticeList.get(pos);
	}

	@Override
	public long getItemId(int pos) {
		return pos;
	}

	@Override
	public View getView(int pos, View convertView, ViewGroup parent) {
		ViewHolder holder = null;

		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_notice, null);

			holder = new ViewHolder();
			holder.tvTime = (TextView) convertView.findViewById(R.id.tv_create_time);
			holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
			holder.tvContent = (TextView) convertView.findViewById(R.id.tv_content);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		Notice notice = mNoticeList.get(pos);
		if (notice != null) {
			holder.tvTime.setText(DateTimeUtil.toStandardTime(notice.getCreateTime()));
			holder.tvTitle.setText(notice.getTitle());
			holder.tvContent.setText(notice.getContent());
		}

		return convertView;
	}

	private final class ViewHolder {
		public TextView tvTime;
		public TextView tvTitle;
		public TextView tvContent;
	}
}
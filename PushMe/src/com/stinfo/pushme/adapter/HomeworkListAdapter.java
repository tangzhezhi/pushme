package com.stinfo.pushme.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.stinfo.pushme.MyBaseAdatper;
import com.stinfo.pushme.R;
import com.stinfo.pushme.entity.Homework;
import com.stinfo.pushme.util.DateTimeUtil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class HomeworkListAdapter extends MyBaseAdatper {

	private HashMap<String, Integer> mapIcon = new HashMap<String, Integer>();
	private LayoutInflater mInflater;
	private ArrayList<Homework> mHomeworkList;

	public HomeworkListAdapter(Context context, ArrayList<Homework> homeworkList) {
		super();
		mHomeworkList = homeworkList;
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		mapIcon.put("全部", R.drawable.work_qita);
		mapIcon.put("语文", R.drawable.work_yuwen);
		mapIcon.put("英语", R.drawable.work_yinyu);
		mapIcon.put("历史", R.drawable.work_lishi);
		mapIcon.put("生物", R.drawable.work_shengwu);
		mapIcon.put("政治", R.drawable.work_zhenzhi);
		mapIcon.put("地理", R.drawable.work_dili);
		mapIcon.put("化学", R.drawable.work_huaxue);
		mapIcon.put("数学", R.drawable.work_shuxue);
		mapIcon.put("物理", R.drawable.work_wuli);
	}

	@Override
	public int getCount() {
		return mHomeworkList.size();
	}

	@Override
	public Object getItem(int pos) {
		return mHomeworkList.get(pos);
	}

	@Override
	public long getItemId(int pos) {
		return pos;
	}

	@Override
	public View getView(int pos, View convertView, ViewGroup parent) {
		ViewHolder holder = null;

		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_homework, null);

			holder = new ViewHolder();
			holder.ivSubject = (ImageView) convertView.findViewById(R.id.iv_subject);
			holder.tvTime = (TextView) convertView.findViewById(R.id.tv_create_time);
			holder.tvContent = (TextView) convertView.findViewById(R.id.tv_content);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		Homework homework = mHomeworkList.get(pos);
		if (homework != null) {
			int resId = mapIcon.get(homework.getSubject());
			if (resId < 1) {
				resId = R.drawable.work_qita;
			}
			holder.ivSubject.setImageResource(resId);
			holder.tvTime.setText(DateTimeUtil.toStandardTime(homework.getCreateTime()));
			holder.tvContent.setText(homework.getContent());
		}

		return convertView;
	}

	private final class ViewHolder {
		public ImageView ivSubject;
		public TextView tvTime;
		public TextView tvContent;
	}
}
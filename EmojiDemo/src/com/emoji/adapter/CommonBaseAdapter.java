package com.emoji.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * 
 * @author tangjiabing
 * 
 * @see 开源时间：2016年03月31日
 * 
 *      记得给我个star哦~
 * 
 */
public abstract class CommonBaseAdapter<T> extends BaseAdapter {

	private Context mContext = null;
	private int mLayoutId = 0;
	private ArrayList<T> mDataList = null;

	public CommonBaseAdapter(Context context, int layoutId,
			ArrayList<T> dataList) {
		mContext = context;
		mLayoutId = layoutId;
		mDataList = dataList;
	}

	@Override
	public int getCount() {
		return mDataList.size();
	}

	@Override
	public Object getItem(int position) {
		return mDataList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = ViewHolder.getInstance(mContext, mLayoutId,
				convertView, position);
		T t = mDataList.get(position);
		convert(holder, t);
		return holder.getConvertView();
	}

	/**
	 * 在此方法中设置列表项中所有控件的显示
	 * 
	 * @param holder
	 * @param t
	 *            列表项的数据信息
	 * 
	 */
	public abstract void convert(ViewHolder holder, T t);

}

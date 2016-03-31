package com.emoji.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;

/**
 * 
 * @author tangjiabing
 * 
 * @see 开源时间：2016年03月31日
 * 
 *      记得给我个star哦~
 * 
 */
public class ViewHolder {

	private int mPosition = 0;
	private SparseArray<View> mViewArray = null;
	private View mConvertView = null;

	private ViewHolder(Context context, int layoutId, View convertView,
			int position) {
		mPosition = position;
		mViewArray = new SparseArray<View>();
		mConvertView = LayoutInflater.from(context).inflate(layoutId, null);
		mConvertView.setTag(this);
	}

	/**
	 * 获取ViewHolder实例
	 * 
	 * @param context
	 * @param layoutId
	 * @param convertView
	 * @param position
	 * @return
	 */
	public static ViewHolder getInstance(Context context, int layoutId,
			View convertView, int position) {
		ViewHolder holder = null;
		if (convertView == null)
			holder = new ViewHolder(context, layoutId, convertView, position);
		else
			holder = (ViewHolder) convertView.getTag();
		return holder;
	}

	/**
	 * 获取列表项中指定的控件
	 * 
	 * @param viewId
	 *            控件id
	 * @return
	 */
	public <T extends View> T getView(int viewId) {
		View view = mViewArray.get(viewId);
		if (view == null) {
			view = mConvertView.findViewById(viewId);
			mViewArray.put(viewId, view);
		}
		return (T) view;
	}

	public View getConvertView() {
		return mConvertView;
	}

	public int getPosition() {
		return mPosition;
	}

}

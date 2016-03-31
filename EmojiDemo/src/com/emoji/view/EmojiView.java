package com.emoji.view;

import java.lang.reflect.Field;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.emoji.adapter.CommonBaseAdapter;
import com.emoji.adapter.CommonPagerAdapter;
import com.emoji.adapter.ViewHolder;
import com.emoji.bean.ResultBean;
import com.emoji.res.DimenRes;
import com.emoji.res.DrawableRes;
import com.emoji.res.LayoutRes;
import com.emoji.res.ViewRes;
import com.emoji.util.CameraAppUtil;
import com.emoji.util.CropPictureAppUtil;
import com.emoji.util.DiskCacheUtil;
import com.emoji.util.EmojiUtil;
import com.emoji.util.GalleryAppUtil;
import com.emoji.util.ResUtil;

/**
 * 
 * @author tangjiabing
 * 
 * @see 开源时间：2016年03月31日
 * 
 *      记得给我个star哦~
 * 
 */
public class EmojiView extends LinearLayout {

	/** 每一行显示表情符的个数 */
	private static final int EACH_ROW_EMOJI_NUMBER = 7;
	/** 每一页显示表情符的个数 */
	private static final int EACH_PAGE_EMOJI_NUMBER = 20;
	/** 表情符的总个数 */
	private static final int TOTAL_EMOJI_NUMBER = 60;
	/** drawable文件夹中表情图标名称的前缀 */
	private static final String EMOJI_FILE_NAME_PREFIX = "ee_static_";
	/** 删除符的名称 */
	private static final String EMOJI_DELETE_NAME = "emoji_delete";
	/** 反射类的路径 */
	private static final String EMOJI_REFLECTION_PATH = "com.emoji.util.EmojiUtil";
	/** 打开图库 */
	private static final int ACTION_GALLERY = 0x11;
	/** 打开相机 */
	private static final int ACTION_CAMERA = 0x12;
	/** 裁剪图片 */
	private static final int ACTION_CROP = 0x13;

	private ImageView mAudioImageView = null;
	private EditText mContentText = null;
	private ImageView mSmileImageView = null;
	private ImageView mAddImageView = null;
	private Button mSendButton = null;
	private LinearLayout mSmileLinearLayout = null;
	private ViewPager mSmileViewPager = null;
	private LinearLayout mDotLinearLayout = null;
	private LinearLayout mAddLinearLayout = null;
	private GridView mAddGridView = null;
	private InputMethodManager mInputManager = null;
	private ArrayList<View> mDotViewList = null;
	private ArrayList<String> mEmojiResList = null;
	private Handler mHandler = null;
	private DeleteThread mDeleteThread = null;
	private boolean mIsStartDeleteThread = false;
	private int mAddGridItemAction = 0;
	private OnClickSendBtnListener mSendBtnListener = null;
	private ResUtil mResUtil = null;

	public EmojiView(Context context) {
		this(context, null);
	}

	public EmojiView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public EmojiView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setOrientation(LinearLayout.VERTICAL);
		mResUtil = new ResUtil(context);
		View.inflate(context, mResUtil.getIdFromLayout(LayoutRes.emojiview),
				this);
		findView();
		init();
		registerListener();
	}

	// ****************************************************************************
	// findView，init，registerListener

	private void findView() {
		mAudioImageView = (ImageView) findViewById(mResUtil
				.getIdFromView(ViewRes.audioImageView));
		mContentText = (EditText) findViewById(mResUtil
				.getIdFromView(ViewRes.contentText));
		mSmileImageView = (ImageView) findViewById(mResUtil
				.getIdFromView(ViewRes.smileImageView));
		mAddImageView = (ImageView) findViewById(mResUtil
				.getIdFromView(ViewRes.addImageView));
		mSendButton = (Button) findViewById(mResUtil
				.getIdFromView(ViewRes.sendButton));
		mSmileLinearLayout = (LinearLayout) findViewById(mResUtil
				.getIdFromView(ViewRes.smileLinearLayout));
		mSmileViewPager = (ViewPager) findViewById(mResUtil
				.getIdFromView(ViewRes.smileViewPager));
		mDotLinearLayout = (LinearLayout) findViewById(mResUtil
				.getIdFromView(ViewRes.dotLinearLayout));
		mAddLinearLayout = (LinearLayout) findViewById(mResUtil
				.getIdFromView(ViewRes.addLinearLayout));
		mAddGridView = (GridView) findViewById(mResUtil
				.getIdFromView(ViewRes.addGridView));
	}

	private void init() {
		// 初始化输入（键盘）管理服务对象
		mInputManager = (InputMethodManager) getContext().getSystemService(
				Context.INPUT_METHOD_SERVICE);
		mHandler = new Handler();
		initEmojiResList();
		initSmileViewPager();
		initAddGridView();
	}

	private void registerListener() {
		mContentText.setOnClickListener(new ViewClickListener());
		mContentText.setOnTouchListener(new EditTouchListener());
		mContentText.addTextChangedListener(new TextChangedListener());
		mSmileImageView.setOnClickListener(new ViewClickListener());
		mAddImageView.setOnClickListener(new ViewClickListener());
		mSendButton.setOnClickListener(new ViewClickListener());
		mSmileViewPager
				.setOnPageChangeListener(new ViewPagerOnPageChangeListener());
		mAddGridView.setOnItemClickListener(new AddGridItemClickListener());
	}

	// ****************************************************************************
	// 公有方法

	public void setOnClickSendBtnListener(OnClickSendBtnListener listener) {
		mSendBtnListener = listener;
	}

	public void setContentText(CharSequence text) {
		mContentText.setText(EmojiUtil.getEmojiText(getContext(), text));
	}

	public void setContentHint(CharSequence hint) {
		mContentText.setHint(hint);
	}

	public void showKeyboard() {
		mContentText.requestFocus();
		mInputManager.showSoftInput(mContentText,
				InputMethodManager.SHOW_IMPLICIT);
		mSmileLinearLayout.setVisibility(View.GONE);
		mAddLinearLayout.setVisibility(View.GONE);
	}

	public void clearDiskCache() {
		DiskCacheUtil.clearDiskCache(getContext());
	}

	public void onResume() {
		setContentText(mContentText.getText());
	}

	public void onDestroy() {
		DiskCacheUtil.close(getContext());
	}

	public ResultBean onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		boolean result = false;
		String path = null;
		if (mAddGridItemAction == ACTION_GALLERY
				|| mAddGridItemAction == ACTION_CAMERA) {
			ResultBean bean = null;
			if (mAddGridItemAction == ACTION_GALLERY)
				bean = GalleryAppUtil.onActivityResult(getContext(),
						requestCode, resultCode, intent);
			else
				bean = CameraAppUtil.onActivityResult(getContext(),
						requestCode, resultCode, intent);
			if (bean.result) {
				result = bean.result;
				CropPictureAppUtil.cropPicture(getContext(),
						Uri.parse("file:///" + bean.path));
				mAddGridItemAction = ACTION_CROP;
			}
		} else if (mAddGridItemAction == ACTION_CROP) {
			ResultBean bean = CropPictureAppUtil.onActivityResult(getContext(),
					requestCode, resultCode, intent);
			if (bean.result) {
				result = bean.result;
				path = bean.path;
			}
		}
		return new ResultBean(result, path);
	}

	// ****************************************************************************
	// 私有方法

	/**
	 * 加载drawable中的表情文件名
	 */
	private void initEmojiResList() {
		mEmojiResList = new ArrayList<String>();
		for (int i = 0; i < TOTAL_EMOJI_NUMBER; i++) {
			String fileName = EMOJI_FILE_NAME_PREFIX;
			if (i <= 9)
				fileName = fileName + "00" + i;
			else if (9 < i && i < 100)
				fileName = fileName + "0" + i;
			mEmojiResList.add(fileName);
		}
	}

	/**
	 * 初始化SmileViewPager
	 */
	private void initSmileViewPager() {
		int dotWH = (int) getResources().getDimension(
				mResUtil.getIdFromDimen(DimenRes.emoji_dotView_wh));
		int dotMarginLR = (int) getResources().getDimension(
				mResUtil.getIdFromDimen(DimenRes.emoji_dotView_margin_lr));
		int pageNum = (int) Math.ceil(TOTAL_EMOJI_NUMBER / 1.0
				/ EACH_PAGE_EMOJI_NUMBER);
		ArrayList<View> pagerViewList = new ArrayList<View>(pageNum);
		mDotViewList = new ArrayList<View>(pageNum);
		for (int i = 0; i < pageNum; i++) {
			View dotView = View.inflate(getContext(),
					mResUtil.getIdFromLayout(LayoutRes.emoji_view_dot), null);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					dotWH, dotWH);
			params.leftMargin = dotMarginLR;
			params.rightMargin = dotMarginLR;
			mDotLinearLayout.addView(dotView, params);
			mDotViewList.add(dotView);
			if (i == 0)
				dotView.setSelected(true);

			GridView gridView = (GridView) View.inflate(getContext(), mResUtil
					.getIdFromLayout(LayoutRes.emoji_viewpager_item_smile),
					null);
			int end = (i + 1) * EACH_PAGE_EMOJI_NUMBER;
			if (end > mEmojiResList.size())
				end = mEmojiResList.size();
			ArrayList<String> dataList = subList(mEmojiResList, i
					* EACH_PAGE_EMOJI_NUMBER, end);
			dataList.add(EMOJI_DELETE_NAME);
			SmileGridAdapter adapter = new SmileGridAdapter(
					getContext(),
					mResUtil.getIdFromLayout(LayoutRes.emoji_gridview_item_smile),
					dataList);
			gridView.setAdapter(adapter);
			gridView.setOnItemClickListener(new SmileGridItemClickListener());
			pagerViewList.add(gridView);
		}
		CommonPagerAdapter pagerAdapter = new CommonPagerAdapter(pagerViewList);
		mSmileViewPager.setAdapter(pagerAdapter);
	}

	private void initAddGridView() {
		ArrayList<Integer> dataList = new ArrayList<Integer>(2);
		dataList.add(mResUtil.getIdFromDrawable(DrawableRes.emoji_picture));
		dataList.add(mResUtil.getIdFromDrawable(DrawableRes.emoji_camera));
		AddGridAdapter adapter = new AddGridAdapter(getContext(),
				mResUtil.getIdFromLayout(LayoutRes.emoji_gridview_item_add),
				dataList);
		mAddGridView.setAdapter(adapter);
	}

	/**
	 * 隐藏软键盘
	 */
	private void hideKeyboard() {
		Activity activity = (Activity) getContext();
		if (activity.getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
			if (activity.getCurrentFocus() != null)
				mInputManager.hideSoftInputFromWindow(activity
						.getCurrentFocus().getWindowToken(),
						InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

	/**
	 * 改变圆点的选中状态
	 * 
	 * @param selectIndex
	 */
	private void changeDotStatus(int selectIndex) {
		for (int i = 0; i < mDotViewList.size(); i++) {
			View dot = mDotViewList.get(i);
			if (i == selectIndex)
				dot.setSelected(true);
			else
				dot.setSelected(false);
		}
	}

	/**
	 * 截取数据集中的某一区域
	 * 
	 * @param parentList
	 * @param start
	 * @param end
	 * @return
	 */
	private <T> ArrayList<T> subList(ArrayList<T> parentList, int start, int end) {
		ArrayList<T> subList = new ArrayList<T>(end - start);
		for (int i = start; i < parentList.size() && i < end; i++) {
			T t = parentList.get(i);
			subList.add(t);
		}
		return subList;
	}

	/**
	 * 删除文本或表情
	 */
	private void deleteTextOrEmoji() {
		int selectionStart = mContentText.getSelectionStart(); // 获取光标位置
		Editable editable = mContentText.getEditableText();
		String content = mContentText.getText().toString();
		if ("".equals(content) == false && selectionStart > 0) {
			String tempStr = content.substring(0, selectionStart);
			int start = tempStr.lastIndexOf("[");
			int end = tempStr.lastIndexOf("]");
			if (start != -1 && end != -1 && start < end
					&& end + 1 == selectionStart) { // 删除表情
				String biaoqing = tempStr.substring(start, selectionStart);
				if (EmojiUtil.containsKey(biaoqing)) // 存在该表情，即删除该表情
					editable.delete(start, selectionStart);
				else
					// 不存在该表情，即删除文字
					editable.delete(selectionStart - 1, selectionStart);
			} else
				// 删除文字
				editable.delete(selectionStart - 1, selectionStart);
		}
	}

	// ****************************************************************************
	// 自定义的类

	public interface OnClickSendBtnListener {
		public void send(Editable editable);
	}

	private class EditTouchListener implements OnTouchListener {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// 解决scrollView中嵌套EditText导致不能上下滑动的问题
			v.getParent().requestDisallowInterceptTouchEvent(true);
			switch (event.getAction() & MotionEvent.ACTION_MASK) {
			case MotionEvent.ACTION_UP:
				v.getParent().requestDisallowInterceptTouchEvent(false);
				break;
			}
			return false;
		}
	}

	private class ViewClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			int id = v.getId();
			if (id == mResUtil.getIdFromView(ViewRes.smileImageView)) {
				if (mSmileLinearLayout.getVisibility() == View.VISIBLE)
					mSmileLinearLayout.setVisibility(View.GONE);
				else {
					mSmileLinearLayout.setVisibility(View.VISIBLE);
					mAddLinearLayout.setVisibility(View.GONE);
					hideKeyboard();
				}
			} else if (id == mResUtil.getIdFromView(ViewRes.contentText)) {
				mSmileLinearLayout.setVisibility(View.GONE);
				mAddLinearLayout.setVisibility(View.GONE);
			} else if (id == mResUtil.getIdFromView(ViewRes.sendButton)) {
				if (mSendBtnListener != null)
					mSendBtnListener.send(mContentText.getText());
				mContentText.setText("");
			} else if (id == mResUtil.getIdFromView(ViewRes.addImageView)) {
				if (mAddLinearLayout.getVisibility() == View.VISIBLE)
					mAddLinearLayout.setVisibility(View.GONE);
				else {
					mAddLinearLayout.setVisibility(View.VISIBLE);
					mSmileLinearLayout.setVisibility(View.GONE);
					hideKeyboard();
				}
			}
		}
	}

	private class ViewPagerOnPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageSelected(int arg0) {
			changeDotStatus(arg0);
		}
	}

	private class SmileGridAdapter extends CommonBaseAdapter<String> {

		private Resources resources = null;
		private String packageName = null;

		public SmileGridAdapter(Context context, int layoutId,
				ArrayList<String> dataList) {
			super(context, layoutId, dataList);
			resources = context.getResources();
			packageName = context.getPackageName();
		}

		@Override
		public void convert(ViewHolder holder, String t) {
			ImageView iconImageView = holder.getView(mResUtil
					.getIdFromView(ViewRes.iconImageView));
			int resId = resources.getIdentifier(t, "drawable", packageName);
			iconImageView.setImageResource(resId);
			if (EMOJI_DELETE_NAME.equals(t)) {
				View convertView = holder.getConvertView();
				convertView
						.setBackgroundResource(mResUtil
								.getIdFromDrawable(DrawableRes.emoji_selector_delete_gridview_item_smile));
				convertView.setOnTouchListener(new DeleteTouchListener());
				convertView.setOnClickListener(new DeleteClickListener());
				convertView
						.setOnLongClickListener(new DeleteLongClickListener());
			}
		}
	}

	private class SmileGridItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			try {
				String fileName = (String) parent.getItemAtPosition(position);
				if (EMOJI_DELETE_NAME.equals(fileName) == false) { // 插入表情
					int selectionStart = mContentText.getSelectionStart(); // 获取光标位置
					Editable editable = mContentText.getEditableText();
					Class<?> clz = Class.forName(EMOJI_REFLECTION_PATH);
					Field field = clz.getField(fileName);
					editable.insert(
							selectionStart,
							EmojiUtil.getEmojiText(getContext(),
									(String) field.get(null)));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private class AddGridAdapter extends CommonBaseAdapter<Integer> {

		public AddGridAdapter(Context context, int layoutId,
				ArrayList<Integer> dataList) {
			super(context, layoutId, dataList);
		}

		@Override
		public void convert(ViewHolder holder, Integer t) {
			ImageView iconImageView = holder.getView(mResUtil
					.getIdFromView(ViewRes.iconImageView));
			iconImageView.setImageResource(t);
		}

	}

	private class AddGridItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			int resId = (Integer) parent.getItemAtPosition(position);
			if (resId == mResUtil.getIdFromDrawable(DrawableRes.emoji_picture)) {
				mAddGridItemAction = ACTION_GALLERY;
				GalleryAppUtil.openGalleryApp(getContext());
			} else if (resId == mResUtil
					.getIdFromDrawable(DrawableRes.emoji_camera)) {
				mAddGridItemAction = ACTION_CAMERA;
				CameraAppUtil.openCameraApp(getContext());
			}
		}
	}

	private class TextChangedListener implements TextWatcher {

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
		}

		@Override
		public void afterTextChanged(Editable s) {
			if (s.toString().equals("") == false) {
				mAddImageView.setVisibility(View.GONE);
				mSendButton.setVisibility(View.VISIBLE);
			} else {
				mAddImageView.setVisibility(View.VISIBLE);
				mSendButton.setVisibility(View.GONE);
			}
		}

	}

	private class DeleteLongClickListener implements OnLongClickListener {
		@Override
		public boolean onLongClick(View view) {
			if (mIsStartDeleteThread == false) {
				mIsStartDeleteThread = true;
				mDeleteThread = new DeleteThread();
				mDeleteThread.start();
			}
			return false;
		}
	}

	private class DeleteClickListener implements OnClickListener {
		@Override
		public void onClick(View view) {
			deleteTextOrEmoji();
		}
	}

	private class DeleteTouchListener implements OnTouchListener {
		@Override
		public boolean onTouch(View view, MotionEvent event) {
			if (event.getAction() == MotionEvent.ACTION_UP) {
				if (mDeleteThread != null) {
					mDeleteThread.finish();
					mDeleteThread = null;
					mIsStartDeleteThread = false;
				}
			}
			return false;
		}
	}

	private class DeleteThread extends Thread {

		private boolean stop = false;

		@Override
		public void run() {
			while (!stop) {
				try {
					Thread.sleep(10);
					mHandler.post(new Runnable() {
						@Override
						public void run() {
							deleteTextOrEmoji();
						}
					});
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

		public void finish() {
			stop = true;
		}
	}

}

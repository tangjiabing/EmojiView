package com.emoji.util;

import java.io.File;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;

import com.emoji.bean.ResultBean;
import com.emoji.global.EmojiGlobal;

/**
 * 
 * @author tangjiabing
 * 
 * @see 开源时间：2016年03月31日
 * 
 *      记得给我个star哦~
 * 
 */
public class CameraAppUtil {

	private static final String PICTURE_SUFFIX = ".jpg";
	private static String mCurrentFilePath = null;

	public static void openCameraApp(Context context) {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		mCurrentFilePath = DiskCacheUtil.getDiskCacheDir(context)
				.getAbsolutePath()
				+ File.separator
				+ FileNameUtil.getNameBySystemTime(PICTURE_SUFFIX);
		intent.putExtra(MediaStore.EXTRA_OUTPUT,
				Uri.fromFile(new File(mCurrentFilePath)));
		((Activity) context).startActivityForResult(intent,
				EmojiGlobal.REQUEST_CODE_CAMERA);
	}

	public static ResultBean onActivityResult(Context context, int requestCode,
			int resultCode, Intent intent) {
		boolean result = false;
		String path = null;
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == EmojiGlobal.REQUEST_CODE_CAMERA) {
				path = mCurrentFilePath;
				DiskCacheUtil cacheUtil = DiskCacheUtil.open(context);
				cacheUtil.put(path);
				result = true;
			}
		}
		ResultBean bean = new ResultBean(result, path);
		return bean;
	}

}

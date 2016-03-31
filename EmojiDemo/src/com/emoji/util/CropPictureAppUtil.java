package com.emoji.util;

import java.io.File;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

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
public class CropPictureAppUtil {

	private static final String PICTURE_SUFFIX = ".jpg";
	private static final String OUTPUT_FORMAT = "JPEG";
	private static String mCurrentFilePath = null;

	public static void cropPicture(Context context, Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");

		// aspectX，aspectY：宽高的比例
		// intent.putExtra("aspectX", 1);
		// intent.putExtra("aspectY", 1);

		// outputX，outputY：裁剪图片的宽高
		// intent.putExtra("outputX", 300);
		// intent.putExtra("outputY", 300);

		// intent.putExtra("return-data", true);

		mCurrentFilePath = DiskCacheUtil.getDiskCacheDir(context)
				.getAbsolutePath()
				+ File.separator
				+ FileNameUtil.getNameBySystemTime(PICTURE_SUFFIX);
		intent.putExtra("output", Uri.fromFile(new File(mCurrentFilePath)));
		intent.putExtra("outputFormat", OUTPUT_FORMAT);
		((Activity) context).startActivityForResult(intent,
				EmojiGlobal.REQUEST_CODE_CROP);
	}

	public static ResultBean onActivityResult(Context context, int requestCode,
			int resultCode, Intent intent) {
		boolean result = false;
		String path = null;
		if (resultCode == Activity.RESULT_OK
				&& requestCode == EmojiGlobal.REQUEST_CODE_CROP) {

			// Bitmap bitmap = intent.getParcelableExtra("data");

			path = mCurrentFilePath;
			DiskCacheUtil cacheUtil = DiskCacheUtil.open(context);
			cacheUtil.put(path);
			result = true;
		}
		ResultBean bean = new ResultBean(result, path);
		return bean;
	}

}

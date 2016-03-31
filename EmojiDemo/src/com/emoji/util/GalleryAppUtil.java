package com.emoji.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

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
public class GalleryAppUtil {

	public static void openGalleryApp(Context context) {
		Intent intent = null;
		boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
		// 在4.3或以下，可以直接用ACTION_GET_CONTENT；
		// 但在4.4或以上，官方建议用ACTION_OPEN_DOCUMENT。其实区别不大，真正的区别在于返回的Uri
		if (isKitKat == false)
			intent = new Intent(Intent.ACTION_GET_CONTENT);
		else
			intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
		intent.setType("image/*");
		Intent wrapperIntent = Intent.createChooser(intent, null);
		((Activity) context).startActivityForResult(wrapperIntent,
				EmojiGlobal.REQUEST_CODE_GALLERY);
	}

	public static ResultBean onActivityResult(Context context, int requestCode,
			int resultCode, Intent intent) {
		boolean result = false;
		String path = null;
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == EmojiGlobal.REQUEST_CODE_GALLERY) {
				boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
				path = RealPathUtil
						.getPath(context, intent.getData(), isKitKat);
				result = true;
			}
		}
		ResultBean bean = new ResultBean(result, path);
		return bean;
	}

}

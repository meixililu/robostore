package com.robo.store.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Base64;

public class DrawableUtil {

	public static Bitmap stringtoBitmap(String string) {
		LogUtil.DefalutLog("base64:" + string);
		// 将字符串转换成Bitmap类型
		Bitmap bitmap = null;
		try {
			byte[] bitmapArray;
			bitmapArray = Base64.decode(string, Base64.DEFAULT);
			bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0,
					bitmapArray.length);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bitmap;
	}

	public static Drawable bitmapToDrawable(Bitmap bmp) {
		Drawable drawable = new BitmapDrawable(bmp);
		return drawable;
	}

	public static Drawable base64ToDrawable(String string) {
		Bitmap bmp = stringtoBitmap(string);
		Drawable drawable = new BitmapDrawable(bmp);
		return drawable;
	}

}

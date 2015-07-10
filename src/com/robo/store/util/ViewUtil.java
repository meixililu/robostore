package com.robo.store.util;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.robo.store.R;

public class ViewUtil {

	public static List<ImageView> getImageViewList(Context mContext, int size) {
		List<ImageView> mList = new ArrayList<ImageView>();
		for (int i = 0; i < size; i++) {
			mList.add(getImageView(mContext));
		}
		return mList;
	}

	public static ImageView getImageView(Context mContext) {
		LinearLayout.LayoutParams lp = new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		ImageView mImage = new ImageView(mContext);
		mImage.setImageDrawable(mContext.getResources().getDrawable(
				R.drawable.banner1));
		mImage.setLayoutParams(lp);
		return mImage;
	}
	
	public static View getLine(Context mContext, int color){
		View img = new View(mContext);
		img.setBackgroundColor(mContext.getResources().getColor(color));
		LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,1);
		img.setLayoutParams(mParams);
		return img;
	}
	
	/**
	 * 自己画选中的圆点
	 * 
	 * @param mContext
	 * @return
	 */
	public static ImageView getDot(Context mContext, int index) {
		ImageView img = new ImageView(mContext);
		LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(
				ScreenUtil.dip2px(mContext, 7), ScreenUtil.dip2px(mContext, 7));
		mParams.leftMargin = ScreenUtil.dip2px(mContext, 2);
		mParams.rightMargin = ScreenUtil.dip2px(mContext, 2);
		img.setLayoutParams(mParams);
		img.setBackgroundResource(R.drawable.dot_selector);
		if (index == 0) {
			img.setEnabled(true);
		} else {
			img.setEnabled(false);
		}
		return img;
	}

	public static void changeState(LinearLayout viewpager_dot_layout, int pos) {
		int size = viewpager_dot_layout.getChildCount();
		for (int i = 0; i < size; i++) {
			View mView = viewpager_dot_layout.getChildAt(i);
			if (i == pos) {
				mView.setEnabled(true);
			} else {
				mView.setEnabled(false);
			}
		}
	}
}

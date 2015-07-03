package com.robo.store.util;

import java.util.ArrayList;
import java.util.List;

import com.robo.store.R;

import android.content.Context;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

public class ViewUtil {
	
	public static List<ImageView> getImageViewList(Context mContext,int size){
		List<ImageView> mList = new ArrayList<ImageView>();
		for(int i=0; i<size; i++){
			mList.add(getImageView(mContext));
		}
		return mList;
	}

	public static ImageView getImageView(Context mContext){
		LinearLayout.LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		ImageView mImage = new ImageView(mContext);
		mImage.setImageDrawable(mContext.getResources().getDrawable(R.drawable.banner1));
		mImage.setLayoutParams(lp);
		return mImage;
	}
}

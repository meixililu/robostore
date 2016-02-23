package com.robo.store.util;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.robo.store.R;
import com.robo.store.dao.MachineVO;

public class ViewUtil {

	public static void addQuhuoShopMachine(Context mContext, LinearLayout layout,List<MachineVO> list){
		int size = list.size();
		LinearLayout hanglayout = null;
		for(int i=0; i<size; i++){
			MachineVO mMachineVO = list.get(i);
			if(i%3 == 0){
				hanglayout = initLinearLayout(mContext);
				layout.addView(hanglayout);
			}
			if(hanglayout != null){
				LinearLayout ItemLinearLayout = initItemLinearLayout(mContext);
				ItemLinearLayout.addView( initTextView(mContext,mMachineVO.getMachineName()) );
				hanglayout.addView(ItemLinearLayout);
			}
		}
	}
	
	public static LinearLayout initLinearLayout(Context mContext){
		LinearLayout layout = new LinearLayout(mContext);
		LinearLayout.LayoutParams lp = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		layout.setWeightSum(3);
		layout.setOrientation(LinearLayout.HORIZONTAL);
		layout.setLayoutParams(lp);
		return layout;
	}
	
	public static LinearLayout initItemLinearLayout(Context mContext){
		LinearLayout layout = new LinearLayout(mContext);
		LinearLayout.LayoutParams lp = new LayoutParams(0, LayoutParams.WRAP_CONTENT);
		lp.weight = 1;
		layout.setOrientation(LinearLayout.HORIZONTAL);
		layout.setLayoutParams(lp);
		return layout;
	}
	
	public static TextView initTextView(Context mContext, String content){
		TextView tv = new TextView(mContext);
		LinearLayout.LayoutParams lp = new LayoutParams(0, ScreenUtil.dip2px(mContext, 37));
		lp.topMargin = ScreenUtil.dip2px(mContext, 10);
		lp.leftMargin = ScreenUtil.dip2px(mContext, 5);
		lp.rightMargin = ScreenUtil.dip2px(mContext, 5);
		lp.weight = 1;
		tv.setGravity(Gravity.CENTER);
		tv.setLayoutParams(lp);
		tv.setText(content);
		tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
		tv.setTextColor(mContext.getResources().getColor(R.color.white));
		tv.setBackground(mContext.getResources().getDrawable(R.drawable.btn_identity_selector));
		tv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			}
		});
		return tv;
	}
	
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
	
	/**新手引导
	 * @param mContext
	 * @param res
	 * @return
	 */
	public static View getGuideImage(Context mContext,int res){
		ImageView img = new ImageView(mContext);
		ViewGroup.LayoutParams mParams = new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
		img.setLayoutParams(mParams);
		img.setImageResource(res);
		return img;
	}
	
	public static ImageView getGuideImage(Context mContext){
		ImageView img = new ImageView(mContext);
		ViewGroup.LayoutParams mParams = new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
		img.setLayoutParams(mParams);
		img.setScaleType(ScaleType.CENTER_CROP);
		return img;
	}
}

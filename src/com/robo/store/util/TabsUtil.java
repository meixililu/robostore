package com.robo.store.util;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.robo.store.R;


public abstract class TabsUtil {

	public static void initTab(Context context, final LinearLayout tabs, final ViewPager viewPager,int[] titles, int[] drawables){
		for(int i=0; i < titles.length; i++){
			View view = prepareTabView(context, context.getString(titles[i]), drawables[i]);
			view.setTag(i);
			LayoutParams lp = new LayoutParams(0, LayoutParams.MATCH_PARENT);
			lp.weight = 1;
			tabs.addView(view,lp);
			
			view.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					int index = (Integer)v.getTag();
					setCurrentTab(tabs,index);
					viewPager.setCurrentItem(index);
				}
			});
		}
		setCurrentTab(tabs, 0);
	}
	
	public static View prepareTabView(Context context, String text, int drawable) {
        View view = LayoutInflater.from(context).inflate(R.layout.tab_main_nav, null);
        ImageView iv = (ImageView) view.findViewById(R.id.ivIcon);
        iv.setImageResource(drawable);
        TextView tv = (TextView) view.findViewById(R.id.tvTitle);
        tv.setText(text);
        return view;
    }
	
	public static void setCurrentTab(LinearLayout tabs, int index){
		for(int i=0; i<tabs.getChildCount(); i++){
			tabs.getChildAt(i).setSelected(false);
		}
		tabs.getChildAt(index).setSelected(true);
	}
}

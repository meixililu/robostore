package com.robo.store;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.androidquery.AQuery;
import com.robo.store.adapter.ViewPagerAdapter;
import com.robo.store.util.ImageUtil;
import com.robo.store.util.ViewUtil;

public class GuideActivity extends BaseActivity implements OnPageChangeListener {

	private ViewPager viewpager;
	private ViewPagerAdapter vpAdapter;
	private ArrayList<View> views;
	private int currentIndex;
	private AQuery aq;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.guide_layout);
		initViews();
	}

	private void initViews() {
		aq = new AQuery(this);
		viewpager = (ViewPager)findViewById(R.id.view_pager);
		views = new ArrayList<View>();
		ImageView firstView = ViewUtil.getGuideImage(this);
		ImageView secondView = ViewUtil.getGuideImage(this);
		ImageView thirthView = ViewUtil.getGuideImage(this);
		thirthView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				goHome();
			}
		});
		firstView.setImageDrawable(this.getResources().getDrawable(R.drawable.guide_1));
		secondView.setImageDrawable(this.getResources().getDrawable(R.drawable.guide_2));
		thirthView.setImageDrawable(this.getResources().getDrawable(R.drawable.guide_3));
		views.add(firstView);
		views.add(secondView);
		views.add(thirthView);

		vpAdapter = new ViewPagerAdapter(views);
		viewpager.setAdapter(vpAdapter);
		viewpager.setOnPageChangeListener(this);
	}

	@Override
	public void onBackPressed() {
		goHome();
	}

	private void goHome() {
		Intent intent = new Intent();
		intent.setClass(GuideActivity.this, MainActivity.class);
		startActivity(intent);
		finish();
	}

	// 当滑动状态改变时调用
	@Override
	public void onPageScrollStateChanged(int arg0) {
	}

	// 当当前页面被滑动时调用
	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
	}

	// 当新的页面被选中时调用
	@Override
	public void onPageSelected(int arg0) {
	}

}
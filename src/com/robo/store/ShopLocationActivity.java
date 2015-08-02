package com.robo.store;

import android.os.Bundle;

import com.robo.store.adapter.ShopActivityPagerAdapter;
import com.robo.store.util.KeyUtil;
import com.robo.store.util.LogUtil;
import com.robo.store.util.NumberUtil;
import com.robo.store.view.CustomViewPager;
import com.robo.store.view.PagerSlidingTabStrip;

public class ShopLocationActivity extends BaseActivity {

	private PagerSlidingTabStrip indicator;
	private CustomViewPager viewPager;
	private ShopActivityPagerAdapter mAdapter;
	public static double latitude,longitude;
	public static String shopMemo;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(getResources().getString(R.string.shop_location));
		setContentView(R.layout.activity_shop_location);
		init();
	}
	
	private void init(){
		Bundle mBundle = getIntent().getBundleExtra(KeyUtil.BundleKey);
		if(mBundle != null){
			String mLatitude = mBundle.getString(KeyUtil.LatitudeKey);
			String mLongitude = mBundle.getString(KeyUtil.LongitudeKey);
			shopMemo = mBundle.getString(KeyUtil.ShopMemoKey);
			latitude = NumberUtil.StringToDouble(mLatitude);
			longitude = NumberUtil.StringToDouble(mLongitude);
			LogUtil.DefalutLog("latitude:"+latitude+"---"+"longitude:"+longitude);
		}
		viewPager = (CustomViewPager) findViewById(R.id.pager);
		indicator = (PagerSlidingTabStrip) findViewById(R.id.indicator);
		mAdapter = new ShopActivityPagerAdapter(this.getSupportFragmentManager(),this);
		
		viewPager.setAdapter(mAdapter);
		indicator.setViewPager(viewPager);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		latitude = 0;
		longitude = 0;
		shopMemo = null;
	}
	
}

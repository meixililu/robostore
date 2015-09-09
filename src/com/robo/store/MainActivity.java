package com.robo.store;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.gc.materialdesign.widgets.Dialog;
import com.robo.store.adapter.MainTabViewPagerAdapter;
import com.robo.store.util.APKDownloadUtil;
import com.robo.store.util.HttpUtil;
import com.robo.store.util.LogUtil;
import com.robo.store.util.LoginUtil;
import com.robo.store.util.SPUtil;
import com.robo.store.util.TabsUtil;
import com.robo.store.util.WechatPayUtil;
import com.robo.store.wxapi.WXPayEntryActivity;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class MainActivity extends ActionBarActivity implements OnPageChangeListener {

	private LinearLayout tabs;
	private String currentTabId;
	private View currentView;
	private long exitTime = 0;
	private static ViewPager viewPager;
	private List<Fragment> fragmentList;
	private SharedPreferences mSharedPreferences;
	
	private int[] titles = {R.string.tab_main_nav_home, R.string.tab_main_nav_shop, 
			R.string.tab_main_nav_cart, R.string.tab_main_nav_user};
	
	private int[] selecters = {R.drawable.tab_nav_home_selector, R.drawable.tab_nav_shop_selector, 
			R.drawable.tab_nav_cart_selector, R.drawable.tab_nav_user_selector};
	public static IWXAPI msgApi;
	public static MainActivity context;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		setContentView(R.layout.activity_main);
		initWechat();
		init();
		checkNetwork();
		LoginUtil.login(this, mSharedPreferences);
		APKDownloadUtil.CheckUpdate(this,false);
	}
	
	private void checkNetwork(){
		if(!HttpUtil.checkNetwork(this)){
			networdError();
		}
	}
	
	public static void initWechat(){
		msgApi = WXAPIFactory.createWXAPI(context, null);
		msgApi.registerApp(WechatPayUtil.APP_ID);
	}
	
	private void init(){
		mSharedPreferences = SPUtil.getSharedPreferences(this);
		fragmentList = new ArrayList<Fragment>();
		fragmentList.add(new HomeFragment());
		fragmentList.add(new ShopFragment());
		fragmentList.add(new CartFragment());
		fragmentList.add(new UserFragment());
		tabs = (LinearLayout) findViewById(R.id.tabs);
		viewPager = (ViewPager) findViewById(R.id.itemViewPager);
		viewPager.setOffscreenPageLimit(4);
		
		TabsUtil.initTab(this, tabs, viewPager, titles, selecters);
				
		viewPager.setOnPageChangeListener(this);
		viewPager.setAdapter(new MainTabViewPagerAdapter(getSupportFragmentManager(), fragmentList));
	}
	
	private void networdError(){
		Dialog dialog = new Dialog(this, "温馨提示", "当前没有可用的网络连接,请打开网络连接！");
		dialog.addAcceptButton("确定");
		dialog.setOnAcceptButtonClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				checkNetwork();
			}
		});
		dialog.setCancelable(true);
		dialog.show();
	}
	
	@Override
	public void onPageSelected(int arg0) {
		TabsUtil.setCurrentTab(tabs, arg0);
	}
	
	@Override
	public void onPageScrollStateChanged(int arg0) {
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
	}
	
	public static void setCurrentTab(int index){
		viewPager.setCurrentItem(index);
	}

	@Override
	public void onBackPressed() {
    	if ((System.currentTimeMillis() - exitTime) > 2000) {
			Toast.makeText(getApplicationContext(), this.getResources().getString(R.string.exit_program), 0).show();
			exitTime = System.currentTimeMillis();
		} else {
			finish();
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		LogUtil.DefalutLog("MainActivity---onActivityResult");
		if(HomeFragment.RequestCity == requestCode){
			HomeFragment.mBaseFragment.onActivityResult(requestCode, resultCode, data);
		}
		if(UserFragment.requestLoginCode == requestCode){
			UserFragment.mUserFragment.onActivityResult(requestCode, resultCode, data);
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(viewPager != null){
			viewPager = null;
		}
		if(context != null){
			context = null;
		}
		if(msgApi != null){
			msgApi = null;
		}
		LoginUtil.isUpdate = false;
		WXPayEntryActivity.isPaySuccee = false;
	}
	
	
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		getMenuInflater().inflate(R.menu.main, menu);
//		return true;
//	}
//	
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		int id = item.getItemId();
//		if (id == R.id.action_settings) {
//			return true;
//		}
//		return super.onOptionsItemSelected(item);
//	}
}

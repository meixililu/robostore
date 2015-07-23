package com.robo.store;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.Header;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.robo.store.http.TextHttpResponseHandler;
import com.robo.store.adapter.MainTabViewPagerAdapter;
import com.robo.store.http.HttpParameter;
import com.robo.store.http.RoboHttpClient;
import com.robo.store.util.LogUtil;
import com.robo.store.util.LoginUtil;
import com.robo.store.util.SPUtil;
import com.robo.store.util.TabsUtil;
import com.robo.store.util.ToastUtil;
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
		RequestData();
		LoginUtil.login(this, mSharedPreferences);
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
	
	private void RequestData(){
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("currentVer", String.valueOf(HttpParameter.softVerCode));
		RoboHttpClient.get("getAppVersion", params, new TextHttpResponseHandler(){

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
				ToastUtil.diaplayMesLong(MainActivity.this, "连接失败，请重试！");
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, String result) {
				LogUtil.DefalutLog(result);
//				UserLoginResponse mUserLoginResponse = (UserLoginResponse) ResultParse.parseResult(result,UserLoginResponse.class);
//				if(ResultParse.handleResutl(MainActivity.this, mUserLoginResponse)){
//				}
			}
			
			@Override
			public void onFinish() {
			}
		});
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

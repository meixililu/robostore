package com.robo.store;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;

import com.robo.store.http.HttpParameter;
import com.robo.store.util.LoginUtil;
import com.robo.store.util.SPUtil;

public class LoadingActivity extends Activity {

	private SharedPreferences mSharedPreferences;
	private String userName,userPWD;
	//第一次启动的话启动引导界面
	private int isFirstLoad;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_loading);
		mSharedPreferences = SPUtil.getSharedPreferences(this);
		LoginUtil.login(this, mSharedPreferences);
		isFirstLoad = mSharedPreferences.getInt("isFirstLoad", 0);
		new WaitTask().execute();
	}
	
	class WaitTask extends AsyncTask<Void, Void, Void>{

		@Override
		protected Void doInBackground(Void... params) {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			toNextPage();
		}
	}
	
	private void setIsFirstLoad(){
		Editor editor = mSharedPreferences.edit();
		editor.putInt("isFirstLoad", HttpParameter.softVerCode);
		editor.commit();
	}
	
	private void toNextPage(){
		if(isFirstLoad != HttpParameter.softVerCode){
			Intent intent = new Intent(this,GuideActivity.class);
			startActivity(intent);
			setIsFirstLoad();
			finish();
		}else{
			Intent intent = new Intent();
			intent.setClass(this,MainActivity.class);
			startActivity(intent);
			finish();
		}
	}
}

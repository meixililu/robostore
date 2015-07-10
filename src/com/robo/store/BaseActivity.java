package com.robo.store;

import android.content.Intent;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.gc.materialdesign.views.ProgressBarCircularIndeterminate;
import com.robo.store.util.KeyUtil;
import com.robo.store.util.LogUtil;

public class BaseActivity extends ActionBarActivity implements View.OnClickListener{

//	public LinearLayout toolbar;
	public FrameLayout back_cover;
	public TextView titleTv;
	public String title;
	public ProgressBarCircularIndeterminate mProgressbar;
	public SwipeRefreshLayout mSwipeRefreshLayout;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		TransparentStatusbar();
	}
	
	@Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        getActionBarToolbar();
        initProgressbar();
    }

	protected void getActionBarToolbar() {
//        toolbar = (LinearLayout) findViewById(R.id.my_awesome_toolbar);
        back_cover = (FrameLayout) findViewById(R.id.back_cover);
    	if(back_cover != null){
        	back_cover.setOnClickListener(this);
        }
    	titleTv = (TextView) findViewById(R.id.title);
        if(titleTv != null){
        	if(!TextUtils.isEmpty(title)){
        		titleTv.setText(title);
        	}
        }
    }
	
	protected void initProgressbar(){
		if(mProgressbar == null){
			mProgressbar = (ProgressBarCircularIndeterminate) findViewById(R.id.progressBarCircularIndetermininate);
		}
	}
	
	/**
	 * need init beford use
	 */
	protected void initSwipeRefresh(){
		if(mSwipeRefreshLayout == null){
			mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.mswiperefreshlayout);
			mSwipeRefreshLayout.setColorSchemeResources(R.color.holo_blue_bright, 
		            R.color.holo_green_light, 
		            R.color.holo_orange_light, 
		            R.color.holo_red_light);
			mSwipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
				@Override
				public void onRefresh() {
					onSwipeRefreshLayoutRefresh();
				}
			});
		}
	}
	
	
	public void onSwipeRefreshLayoutFinish(){
		if(mSwipeRefreshLayout != null){
			mSwipeRefreshLayout.setRefreshing(false);
		}
	}
	
	public void onSwipeRefreshLayoutRefresh(){
	}
	
	public void showProgressbar(){
		if(mProgressbar != null){
			mProgressbar.setVisibility(View.VISIBLE);
		}
	}
	
	public void hideProgressbar(){
		if(mProgressbar != null){
			mProgressbar.setVisibility(View.GONE);
		}
	}
	
	protected void TransparentStatusbar(){
		if(VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
//		StatService.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
//		StatService.onPause(this);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_VOLUME_UP:
//			 AudioTrackUtil.adjustStreamVolume(BaseActivity.this,keyCode);
	         return true;
	    case KeyEvent.KEYCODE_VOLUME_DOWN:
//	    	 AudioTrackUtil.adjustStreamVolume(BaseActivity.this,keyCode);
		     return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	protected void setTitle(String title){
		this.title = title;
	}
	
//	protected boolean toolbarIsShown() {
//        return ViewHelper.getTranslationY(toolbar) == 0;
//    }
//
//	protected boolean toolbarIsHidden() {
//        return ViewHelper.getTranslationY(toolbar) == -toolbar.getHeight();
//    }
//	
//	protected void hideViews() {
//		ObjectAnimator animY = ObjectAnimator.ofFloat(toolbar, "y", -toolbar.getHeight());
//    	animY.setInterpolator(new AccelerateInterpolator(2));
//    	animY.start();
//    }
//
//	protected void showViews() {
//    	ObjectAnimator animY = ObjectAnimator.ofFloat(toolbar, "y", 0);
//    	animY.setInterpolator(new DecelerateInterpolator(2));
//    	animY.start();
//    }
	
	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);
		LogUtil.DefalutLog("BaseActivity---onActivityResult");
	}
	
	protected void toActivity(Class mClass,Bundle bundle){
		Intent intent = new Intent(this,mClass);
		if(bundle != null){
			intent.putExtra(KeyUtil.BundleKey, bundle);
		}
		startActivity(intent);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.back_cover:
			BaseActivity.this.onBackPressed();
			break;
		}
	}
    
}

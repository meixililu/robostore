package com.robo.store;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class BaseFragment extends Fragment {

	//缓存Fragment view
	protected View rootView;  
	protected int layoutId;
	private boolean mHasLoadedOnce;
	 
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		if(rootView==null){  
            rootView = inflater.inflate(layoutId, null);  
        }  
        ViewGroup parent = (ViewGroup) rootView.getParent();  
        if (parent != null) {  
            parent.removeView(rootView);  
        }   
        return rootView;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initView();
	}
	
	protected void initView(){
	}
	
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if(isVisibleToUser && !mHasLoadedOnce){
			mHasLoadedOnce = true;
			loadData();
		}
	}
	
	protected void loadData(){
	}
	
	protected void setLayoutId(int layoutid){
		layoutId = layoutid;
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}
	
}

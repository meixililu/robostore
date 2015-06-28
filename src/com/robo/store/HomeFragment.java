package com.robo.store;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.robo.store.util.LogUtil;

public class HomeFragment extends BaseFragment implements OnClickListener{

	private TextView city;
	private TextView search_btn;
	private FrameLayout city_cover;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		LogUtil.DefalutLog(HomeFragment.class.getName()+"---onCreateView");
		setLayoutId(R.layout.fragment_home);
        return super.onCreateView(inflater, container, savedInstanceState);
	}
	
	protected void initView(){
		city_cover = (FrameLayout) getView().findViewById(R.id.title_cover);
		city = (TextView) getView().findViewById(R.id.title);
		search_btn = (TextView) getView().findViewById(R.id.search_btn);
		
		city_cover.setOnClickListener(this);
		search_btn.setOnClickListener(this);
	}

	
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.title_cover:
			toLocationActivity();
			break;
		case R.id.search_btn:
			toSearchActivity();
			break;
		}
	}
	
	private void toSearchActivity(){
		
	}
	
	private void toLocationActivity(){
		
	}
	
	@Override
	public void onDestroyView() {
		LogUtil.DefalutLog(HomeFragment.class.getName()+"---onDestroyView");
		super.onDestroyView();
	}

}

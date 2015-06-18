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

	private TextView title;
	private TextView search_btn;
	private FrameLayout title_cover;
	
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
		title_cover = (FrameLayout) getView().findViewById(R.id.title_cover);
		title = (TextView) getView().findViewById(R.id.title);
		search_btn = (TextView) getView().findViewById(R.id.search_btn);
		
		title_cover.setOnClickListener(this);
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

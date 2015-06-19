package com.robo.store;

import com.robo.store.util.LogUtil;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

public class CartFragment extends BaseFragment {

	private FrameLayout edit_cover;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		LogUtil.DefalutLog(CartFragment.class.getName()+"---onCreateView");
		setLayoutId(R.layout.fragment_cart);
        return super.onCreateView(inflater, container, savedInstanceState);
	}

	protected void initView(){
		edit_cover = (FrameLayout) getView().findViewById(R.id.edit_cover);
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
	
	@Override
	public void onDestroyView() {
		LogUtil.DefalutLog(CartFragment.class.getName()+"---onDestroyView");
		super.onDestroyView();
	}
	
}

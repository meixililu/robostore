package com.robo.store;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.robo.store.util.LogUtil;

public class ShopMemoFragment extends BaseFragment {

	private LayoutInflater inflater;
	private TextView shop_memo;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		LogUtil.DefalutLog(ShopMemoFragment.class.getName()+"---onCreateView");
		this.inflater = inflater;
		setLayoutId(R.layout.fragment_shop_memo);
        return super.onCreateView(inflater, container, savedInstanceState);
	}
	
	protected void initView(){
		shop_memo = (TextView) getView().findViewById(R.id.shop_memo);
		shop_memo.setText(ShopLocationActivity.shopMemo);
	}
	
	@Override
	public void onDestroyView() {
		LogUtil.DefalutLog(ShopMemoFragment.class.getName()+"---onDestroyView");
		super.onDestroyView();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	}
}

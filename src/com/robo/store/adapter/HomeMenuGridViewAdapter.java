package com.robo.store.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.robo.store.dao.GoodsType;

public class HomeMenuGridViewAdapter extends BaseAdapter {

	private List<GoodsType> GoodsTypeList;
	private Context mContext;
	
	public void HomeMenuGridViewAdapter(Context mContext,List<GoodsType> GoodsTypeList){
		this.mContext = mContext;
		this.GoodsTypeList = GoodsTypeList;
	}
	
	@Override
	public int getCount() {
		return 0;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return null;
	}

}

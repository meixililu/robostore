package com.robo.store.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.robo.store.R;
import com.robo.store.SearchActivity;
import com.robo.store.ShopDetailActivity;
import com.robo.store.dao.ShopBase;
import com.robo.store.util.KeyUtil;
import com.robo.store.view.PinnedSectionListView.PinnedSectionListAdapter;

public class ShopListAdapter extends BaseAdapter implements PinnedSectionListAdapter {

	private Context context;
	private LayoutInflater mInflater;
	private List<ShopBase> AllDataList;
	
	public ShopListAdapter(Context mContext,LayoutInflater mInflater,List<ShopBase> shopList){
		this.context = mContext;
		this.mInflater = mInflater;
		this.AllDataList = shopList;
	}
	
	@Override
	public int getCount() {
		return AllDataList.size();
	}

	@Override
	public ShopBase getItem(int position) {
		return AllDataList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@Override
	public int getItemViewType(int position) {
		ShopBase mShopBase = AllDataList.get(position);
		int type = mShopBase.getType();
		return type;
	}
	
	@Override
	public int getViewTypeCount() {
		return 2;
	}
	
	@Override
	public boolean isItemViewTypePinned(int viewType) {
		return viewType == ShopBase.SECTION;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ShopBase mShopBase = AllDataList.get(position);
		int type = mShopBase.getType();
		ContentHolder holder0 = null;
		SectionHolder holder1 = null;
		if (convertView == null) {
			switch(type){
			case 0:
				holder0 = new ContentHolder();
				convertView = mInflater.inflate(R.layout.shop_list_item_content, null);
				holder0.item_cover = (FrameLayout) convertView.findViewById(R.id.item_cover);
				holder0.shop_name = (TextView) convertView.findViewById(R.id.shop_name);
				holder0.shop_distance = (TextView) convertView.findViewById(R.id.shop_distance);
				convertView.setTag(holder0);
				break;
			case 1:
				holder1 = new SectionHolder();
				convertView = mInflater.inflate(R.layout.shop_list_item_section, null);
				holder1.seciton_name = (TextView) convertView.findViewById(R.id.seciton_name);
				convertView.setTag(holder1);
				break;
			}
		} else {
			switch(type){
			case 0:
				holder0 = (ContentHolder) convertView.getTag();
				break;
			case 1:
				holder1 = (SectionHolder) convertView.getTag();
				break;
			}
		}
		switch(type){
		case 0:
			holder0.shop_name.setText(mShopBase.getShopName());
			holder0.shop_distance.setText(mShopBase.getDistance());
			holder0.item_cover.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Bundle mBundle = new Bundle();
					mBundle.putString(KeyUtil.ShopDetailTitleKey, mShopBase.getShopName());
					mBundle.putString(KeyUtil.ShopDetailIdKey, mShopBase.getShopId());
					Intent intent = new Intent(context,ShopDetailActivity.class);
					intent.putExtra(KeyUtil.BundleKey, mBundle);
					context.startActivity(intent);
				}
			});
			break;
		case 1:
			holder1.seciton_name.setText(mShopBase.getShopName());
			break;
		}
		return convertView;
	}
	
	static class SectionHolder {
		TextView seciton_name;
	}
	
	static class ContentHolder {
		FrameLayout item_cover;
		TextView shop_name;
		TextView shop_distance;
	}

}

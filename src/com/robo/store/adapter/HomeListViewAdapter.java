package com.robo.store.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.robo.store.R;
import com.robo.store.dao.GoodsBase;

public class HomeListViewAdapter extends BaseAdapter {

	private Context context;
	private LayoutInflater mInflater;
	private List<GoodsBase> goodsList;
	
	public HomeListViewAdapter(Context mContext,LayoutInflater mInflater,List<GoodsBase> goodsList){
		this.context = mContext;
		this.mInflater = mInflater;
		this.goodsList = goodsList;
	}
	
	@Override
	public int getCount() {
		return goodsList.size();
	}

	@Override
	public GoodsBase getItem(int position) {
		return goodsList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.home_good_list_item, null);
			holder = new ViewHolder();
			holder.good_icon = (ImageView) convertView.findViewById(R.id.good_icon);
			holder.good_buy = (ImageView) convertView.findViewById(R.id.good_buy);
			holder.good_name = (TextView) convertView.findViewById(R.id.good_name);
			holder.good_des = (TextView) convertView.findViewById(R.id.good_des);
			holder.good_price_new = (TextView) convertView.findViewById(R.id.good_price_new);
			holder.good_price_old = (TextView) convertView.findViewById(R.id.good_price_old);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		GoodsBase mGoodsBase = goodsList.get(position);
		holder.good_name.setText(mGoodsBase.getGoodsName());
		
		return convertView;
	}
	
	static class ViewHolder {
		ImageView good_icon;
		ImageView good_buy;
		TextView good_name;
		TextView good_des;
		TextView good_price_new;
		TextView good_price_old;
	}

}

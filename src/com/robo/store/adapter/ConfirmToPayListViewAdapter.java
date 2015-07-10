package com.robo.store.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.robo.store.R;
import com.robo.store.dao.GoodsBase;
import com.robo.store.util.CartUtil;

public class ConfirmToPayListViewAdapter extends BaseAdapter {

	private Context context;
	private LayoutInflater mInflater;
	private List<GoodsBase> goodsList;
	private TextView total_tv;
	
	public ConfirmToPayListViewAdapter(Context mContext,LayoutInflater mInflater,List<GoodsBase> goodsList,TextView total_tv){
		this.context = mContext;
		this.mInflater = mInflater;
		this.goodsList = goodsList;
		this.total_tv = total_tv;
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
			convertView = mInflater.inflate(R.layout.confirm_to_pay_list_item, null);
			holder = new ViewHolder();
			holder.good_icon = (ImageView) convertView.findViewById(R.id.good_icon);
			holder.goods_number = (TextView) convertView.findViewById(R.id.goods_number);
			holder.good_name = (TextView) convertView.findViewById(R.id.good_name);
			holder.good_price_new = (TextView) convertView.findViewById(R.id.good_price_new);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final GoodsBase mGoodsBase = goodsList.get(position);
		holder.good_name.setText(mGoodsBase.getGoodsName());
		holder.good_price_new.setText("￥" + mGoodsBase.getVipPrice());
		holder.goods_number.setText( "x" + mGoodsBase.getNumber() );
		
		return convertView;
	}
	
	static class ViewHolder {
		ImageView good_icon;
		TextView good_name;
		TextView good_price_new;
		TextView goods_number;
	}

}

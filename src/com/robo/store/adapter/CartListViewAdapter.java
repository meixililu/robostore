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

public class CartListViewAdapter extends BaseAdapter {

	private Context context;
	private LayoutInflater mInflater;
	private List<GoodsBase> goodsList;
	private TextView total_tv;
	
	public CartListViewAdapter(Context mContext,LayoutInflater mInflater,List<GoodsBase> goodsList,TextView total_tv){
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
			convertView = mInflater.inflate(R.layout.cart_list_item, null);
			holder = new ViewHolder();
			holder.good_icon = (ImageView) convertView.findViewById(R.id.good_icon);
			holder.minus_img = (ImageView) convertView.findViewById(R.id.minus_img);
			holder.number_txt = (TextView) convertView.findViewById(R.id.number_txt);
			holder.plus_img = (ImageView) convertView.findViewById(R.id.plus_img);
			holder.good_name = (TextView) convertView.findViewById(R.id.good_name);
			holder.good_price_new = (TextView) convertView.findViewById(R.id.good_price_new);
			holder.get_goods_btn = (Button) convertView.findViewById(R.id.get_goods_btn);
			holder.checkbox = (CheckBox) convertView.findViewById(R.id.checkbox);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final GoodsBase mGoodsBase = goodsList.get(position);
		holder.good_name.setText(mGoodsBase.getGoodsName());
		holder.good_price_new.setText("￥" + mGoodsBase.getVipPrice());
		holder.number_txt.setText(String.valueOf(mGoodsBase.getNumber()));
		holder.checkbox.setChecked(mGoodsBase.isSelected());
		
		holder.get_goods_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
			}
		});
		holder.checkbox.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mGoodsBase.setSelected(holder.checkbox.isChecked());
				CartUtil.setTotalSum(total_tv);
			}
		});
		holder.minus_img.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				holder.number_txt.setText(mGoodsBase.minusNumber());
				if(holder.checkbox.isChecked()){
					CartUtil.setTotalSum(total_tv);
				}
			}
		});
		holder.plus_img.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				holder.number_txt.setText(mGoodsBase.addNumber());
				if(holder.checkbox.isChecked()){
					CartUtil.setTotalSum(total_tv);
				}
			}
		});
		return convertView;
	}
	
	static class ViewHolder {
		CheckBox checkbox;
		ImageView good_icon;
		TextView good_name;
		TextView good_price_new;
		Button get_goods_btn;
		ImageView minus_img;
		ImageView plus_img;
		TextView number_txt;
	}

}

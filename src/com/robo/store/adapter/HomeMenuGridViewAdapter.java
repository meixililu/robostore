package com.robo.store.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.robo.store.R;
import com.robo.store.dao.GoodsType;
import com.robo.store.util.ToastUtil;

public class HomeMenuGridViewAdapter extends BaseAdapter {

	private List<GoodsType> mGoodsTypeList;
	private LayoutInflater inflater;
	private Context mContext;
	
	public void HomeMenuGridViewAdapter(Context mContext, LayoutInflater inflater, List<GoodsType> GoodsTypeList){
		this.mContext = mContext;
		this.mGoodsTypeList = GoodsTypeList;
		this.inflater = inflater;
	}
	
	@Override
	public int getCount() {
		return mGoodsTypeList.size();
	}

	@Override
	public GoodsType getItem(int position) {
		return mGoodsTypeList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.home_menu_gridview_adapter_item, null);
			holder.good_type_name = (TextView) convertView.findViewById(R.id.good_type_name);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final GoodsType mCategory = mGoodsTypeList.get(position);
		holder.good_type_name.setText(mCategory.getGoodsTypeName());
		holder.good_type_name.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ToastUtil.diaplayMesLong(mContext, mCategory.getGoodsTypeName());
			}
		});
		return convertView;
	}
	
	static class ViewHolder {
		TextView good_type_name;
	}

}

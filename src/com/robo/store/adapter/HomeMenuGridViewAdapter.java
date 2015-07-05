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
	
	public HomeMenuGridViewAdapter(Context mContext, LayoutInflater inflater, List<GoodsType> GoodsTypeList){
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
		final GoodsType mGoodsType = mGoodsTypeList.get(position);
		holder.good_type_name.setText(mGoodsType.getGoodsTypeName());
		if(mGoodsType.getIsSelect() == 1){
			holder.good_type_name.setTextColor(mContext.getResources().getColor(R.color.text_dark));
			holder.good_type_name.setBackgroundResource(R.drawable.btn_bg_home_menu_s);
		}else{
			holder.good_type_name.setTextColor(mContext.getResources().getColor(R.color.white));
			holder.good_type_name.setBackgroundResource(R.color.none);
		}
		return convertView;
	}
	
	static class ViewHolder {
		TextView good_type_name;
	}

}

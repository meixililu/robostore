package com.robo.store.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.robo.store.R;
import com.robo.store.dao.GetPickUpLogVO;
import com.robo.store.util.TimeUtil;

public class PickupRecordListAdapter extends BaseAdapter {

	private Context context;
	private LayoutInflater mInflater;
	private List<GetPickUpLogVO> ordersList;
	
	public PickupRecordListAdapter(Context mContext,LayoutInflater mInflater,List<GetPickUpLogVO> goodsList){
		this.context = mContext;
		this.mInflater = mInflater;
		this.ordersList = goodsList;
	}
	
	@Override
	public int getCount() {
		return ordersList.size();
	}

	@Override
	public GetPickUpLogVO getItem(int position) {
		return ordersList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.pickup_record_list_item, null);
			holder = new ViewHolder();
			holder.date_day = (TextView) convertView.findViewById(R.id.date_day);
			holder.date_month = (TextView) convertView.findViewById(R.id.date_month);
			holder.tv_shop = (TextView) convertView.findViewById(R.id.tv_shop);
			holder.tv_machine = (TextView) convertView.findViewById(R.id.tv_machine);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final GetPickUpLogVO mGoodsBase = ordersList.get(position);
		holder.date_day.setText(TimeUtil.customFormatDate(mGoodsBase.getPickUpTime(),TimeUtil.DateFormat,"d")+"日");
		holder.date_month.setText(TimeUtil.customFormatDate(mGoodsBase.getPickUpTime(),TimeUtil.DateFormat,"M")+"月");
		holder.tv_shop.setText(mGoodsBase.getShopName());
		holder.tv_machine.setText(mGoodsBase.getMachineName());
		
		return convertView;
	}
	
	static class ViewHolder {
		TextView date_day;
		TextView date_month;
		TextView tv_shop;
		TextView tv_machine;
	}

}

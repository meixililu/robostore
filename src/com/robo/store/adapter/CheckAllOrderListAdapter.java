package com.robo.store.adapter;

import java.util.HashMap;
import java.util.List;

import org.apache.http.Header;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gc.materialdesign.widgets.Dialog;
import com.robo.store.CheckAllOrdersActivity;
import com.robo.store.ConfirmOrderActivity;
import com.robo.store.OrderDetailActivity;
import com.robo.store.OrderType1Fragment;
import com.robo.store.OrderType2Fragment;
import com.robo.store.OrderType3Fragment;
import com.robo.store.OrderType4Fragment;
import com.robo.store.OrderType5Fragment;
import com.robo.store.OrderType6Fragment;
import com.robo.store.R;
import com.robo.store.dao.GetOrdersListResponse;
import com.robo.store.dao.GetPayParamsRespone;
import com.robo.store.dao.GetSingleOrderResponse;
import com.robo.store.dao.OrderDetailVO;
import com.robo.store.dao.OrderGoods;
import com.robo.store.http.HttpParameter;
import com.robo.store.http.RequestParams;
import com.robo.store.http.RoboHttpClient;
import com.robo.store.http.TextHttpResponseHandler;
import com.robo.store.util.KeyUtil;
import com.robo.store.util.LogUtil;
import com.robo.store.util.ResultParse;
import com.robo.store.util.ToastUtil;
import com.robo.store.util.ViewUtil;
import com.robo.store.util.WechatPayUtil;
import com.squareup.picasso.Picasso;

public class CheckAllOrderListAdapter extends BaseAdapter {

	private CheckAllOrdersActivity context;
	private LayoutInflater mInflater;
	private List<GetOrdersListResponse> goodsList;
	
	public CheckAllOrderListAdapter(CheckAllOrdersActivity mContext,LayoutInflater mInflater,List<GetOrdersListResponse> goodsList){
		this.context = mContext;
		this.mInflater = mInflater;
		this.goodsList = goodsList;
	}
	
	@Override
	public int getCount() {
		return goodsList.size();
	}

	@Override
	public GetOrdersListResponse getItem(int position) {
		return goodsList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@Override
	public int getItemViewType(int position) {
		GetOrdersListResponse mOrderGoods = goodsList.get(position);
		int type = mOrderGoods.getOrderStatus();
		return type;
	}
	
	@Override
	public int getViewTypeCount() {
		return 10;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final GetOrdersListResponse mOrderGoods = goodsList.get(position);
		List<OrderDetailVO> detailList = mOrderGoods.getDetailList();
		int type = mOrderGoods.getOrderStatus();
		ViewHolder1 holder1 = null;
		ViewHolder2 holder2 = null;
		ViewHolder3 holder3 = null;
		ViewHolder4 holder4 = null;
		ViewHolder5 holder5 = null;
		ViewHolder6 holder6 = null;
		if (convertView == null) {
			switch(type){
			case 1:
				holder1 = new ViewHolder1();
				convertView = mInflater.inflate(R.layout.order_list_item_type_1, null);
				holder1.to_pay_btn = (Button) convertView.findViewById(R.id.to_pay_btn);
				holder1.goods_list = (LinearLayout) convertView.findViewById(R.id.goods_list);
				convertView.setTag(holder1);
				break;
			case 2:
				holder2 = new ViewHolder2();
				convertView = mInflater.inflate(R.layout.order_list_item_type_2, null);
				holder2.goods_list = (LinearLayout) convertView.findViewById(R.id.goods_list);
				convertView.setTag(holder2);
				break;
			case 3:
				holder3 = new ViewHolder3();
				convertView = mInflater.inflate(R.layout.order_list_item_type_3, null);
				holder3.refund_btn = (Button) convertView.findViewById(R.id.refund_btn);
				holder3.order_code_img = (TextView) convertView.findViewById(R.id.order_code_img);
				holder3.goods_list = (LinearLayout) convertView.findViewById(R.id.goods_list);
				convertView.setTag(holder3);
				break;
			case 4:
				holder4 = new ViewHolder4();
				convertView = mInflater.inflate(R.layout.order_list_item_type_4, null);
				holder4.order_refund_sum = (TextView) convertView.findViewById(R.id.order_refund_sum);
				holder4.goods_list = (LinearLayout) convertView.findViewById(R.id.goods_list);
				convertView.setTag(holder4);
				break;
			case 5:
				holder5 = new ViewHolder5();
				convertView = mInflater.inflate(R.layout.order_list_item_type_5, null);
				holder5.goods_list = (LinearLayout) convertView.findViewById(R.id.goods_list);
				convertView.setTag(holder5);
				break;
			case 6:
				holder6 = new ViewHolder6();
				convertView = mInflater.inflate(R.layout.order_list_item_type_6, null);
				holder6.goods_list = (LinearLayout) convertView.findViewById(R.id.goods_list);
				convertView.setTag(holder6);
				break;
			}
		} else {
			switch(type){
			case 1:
				holder1 = (ViewHolder1) convertView.getTag();
				break;
			case 2:
				holder2 = (ViewHolder2) convertView.getTag();
				break;
			case 3:
				holder3 = (ViewHolder3) convertView.getTag();
				break;
			case 4:
				holder4 = (ViewHolder4) convertView.getTag();
				break;
			case 5:
				holder5 = (ViewHolder5) convertView.getTag();
				break;
			case 6:
				holder6 = (ViewHolder6) convertView.getTag();
				break;
			}
		}
		switch(type){
		case 1:
			holder1.goods_list.removeAllViews();
			for(int i=0; i<detailList.size(); i++){
				OrderDetailVO mOrderDetailVO = detailList.get(i);
				if(i > 0){
					holder1.goods_list.addView( ViewUtil.getLine(context, R.color.text_grey) );
				}
				View goodsView = getGoodsView(mOrderDetailVO,true,mOrderGoods);
				LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
				goodsView.setLayoutParams(mParams);
				holder1.goods_list.addView(goodsView);
			}
			holder1.to_pay_btn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					getWeChatPara(mOrderGoods.getOrderId());
				}
			});
			break;
		case 2:
			holder2.goods_list.removeAllViews();
			for(int i=0; i<detailList.size(); i++){
				OrderDetailVO mOrderDetailVO = detailList.get(i);
				if(i > 0){
					holder2.goods_list.addView( ViewUtil.getLine(context, R.color.text_grey) );
				}
				View goodsView = getGoodsView(mOrderDetailVO,false,mOrderGoods);
				LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
				goodsView.setLayoutParams(mParams);
				holder2.goods_list.addView(goodsView);
			}
			break;
		case 3:
			holder3.goods_list.removeAllViews();
			for(int i=0; i<detailList.size(); i++){
				OrderDetailVO mOrderDetailVO = detailList.get(i);
				if(i > 0){
					holder3.goods_list.addView( ViewUtil.getLine(context, R.color.text_grey) );
				}
				View goodsView = getGoodsView(mOrderDetailVO,true,mOrderGoods);
				LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
				goodsView.setLayoutParams(mParams);
				holder3.goods_list.addView(goodsView);
			}
			holder3.refund_btn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					refundOrder(mOrderGoods);
				}
			});
			break;
		case 4:
			holder4.order_refund_sum.setText("退款金额：￥" + mOrderGoods.getOrderAmount());
			holder4.goods_list.removeAllViews();
			for(int i=0; i<detailList.size(); i++){
				OrderDetailVO mOrderDetailVO = detailList.get(i);
				if(i > 0){
					holder4.goods_list.addView( ViewUtil.getLine(context, R.color.text_grey) );
				}
				View goodsView = getGoodsView(mOrderDetailVO,false,mOrderGoods);
				LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
				goodsView.setLayoutParams(mParams);
				holder4.goods_list.addView(goodsView);
			}
			break;
		case 5:
			holder5.goods_list.removeAllViews();
			for(int i=0; i<detailList.size(); i++){
				OrderDetailVO mOrderDetailVO = detailList.get(i);
				if(i > 0){
					holder5.goods_list.addView( ViewUtil.getLine(context, R.color.text_grey) );
				}
				View goodsView = getGoodsView(mOrderDetailVO,false,mOrderGoods);
				LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
				goodsView.setLayoutParams(mParams);
				holder5.goods_list.addView(goodsView);
			}
			break;
		case 6:
			holder6.goods_list.removeAllViews();
			for(int i=0; i<detailList.size(); i++){
				OrderDetailVO mOrderDetailVO = detailList.get(i);
				if(i > 0){
					holder6.goods_list.addView( ViewUtil.getLine(context, R.color.text_grey) );
				}
				View goodsView = getGoodsView(mOrderDetailVO,false,mOrderGoods);
				LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
				goodsView.setLayoutParams(mParams);
				holder6.goods_list.addView(goodsView);
			}
			break;
		}
		return convertView;
	}
	
	private View getGoodsView(OrderDetailVO mOrderDetailVO, boolean isShowShopImg, final GetOrdersListResponse mOrdersList){
		OrderGoods mOrderGoods = mOrderDetailVO.getOrderGoods();
		View goodsView = mInflater.inflate(R.layout.order_list_goods_item, null);
		FrameLayout item_cover = (FrameLayout) goodsView.findViewById(R.id.item_cover);
		ImageView good_icon = (ImageView) goodsView.findViewById(R.id.good_icon);
		TextView good_name = (TextView) goodsView.findViewById(R.id.good_name);
		TextView good_price_new = (TextView) goodsView.findViewById(R.id.good_price_new);
		TextView goods_number = (TextView) goodsView.findViewById(R.id.goods_number);
		ImageView get_goods_shop = (ImageView) goodsView.findViewById(R.id.get_goods_shop);
		
		try {
			Picasso.with(context)
			.load(mOrderGoods.getGoodsPic())
			.tag(context)
			.into(good_icon);
		} catch (Exception e) {
			e.printStackTrace();
		}
		good_name.setText(mOrderGoods.getGoodsName());
		good_price_new.setText(mOrderGoods.getGoodsPrise());
		goods_number.setText("x"+mOrderGoods.getGoodsCount());
		item_cover.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				toOrderDetailActivity(mOrdersList);
			}
		});
		if(isShowShopImg){
			get_goods_shop.setVisibility(View.VISIBLE);
			get_goods_shop.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					ToastUtil.diaplayMesShort(context, "可取货店铺");
				}
			});
		}else{
			get_goods_shop.setVisibility(View.GONE);
		}
		return goodsView;
	}
	
	private void getWeChatPara(String orderId){
		final ProgressDialog progressDialog = ProgressDialog.show(context, "", "正在调取支付...", true, false);
		RequestParams params = new RequestParams();
		params.put("orderId", orderId);
		RoboHttpClient.postForPay(HttpParameter.payUrl, params, new TextHttpResponseHandler(){
			
			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
				ToastUtil.diaplayMesLong(context, context.getResources().getString(R.string.connet_fail));
			}
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, String result) {
				GetPayParamsRespone mResponse = (GetPayParamsRespone) ResultParse.parseResult(result,GetPayParamsRespone.class);
				if(ResultParse.handleResutl(context, mResponse)){
					CheckAllOrdersActivity.isNeedRefresh = true;
					WechatPayUtil.startWechat(mResponse.getPayParams());
				}
			}
			
			@Override
			public void onFinish() {
				progressDialog.dismiss();
			}
		});
	}
	
	private void refundOrder(final GetOrdersListResponse mOrderGoods){
		Dialog dialog = new Dialog(context, "温馨提示", "确定要申请退款吗？");
		dialog.addAcceptButton("确定");
		dialog.addCancelButton("取消");
		dialog.setOnAcceptButtonClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				RequestCancleData(mOrderGoods);
			}
		});
		dialog.setCancelable(true);
		dialog.show();
	}
	
	private void RequestCancleData(GetOrdersListResponse mOrderGoods){
		final ProgressDialog progressDialog = ProgressDialog.show(context, "", "正在提交...", true, false);
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("orderId", mOrderGoods.getOrderId());
		params.put("flag", "0");
		RoboHttpClient.post(HttpParameter.orderUrl, "refund", params, new TextHttpResponseHandler(){

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
				ToastUtil.diaplayMesLong(context, "连接失败，请重试！");
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, String result) {
				LogUtil.DefalutLog(result);
				GetSingleOrderResponse mSingleOrder = (GetSingleOrderResponse) ResultParse.parseResult(result,GetSingleOrderResponse.class);
				if(ResultParse.handleResutl(context, mSingleOrder)){
					ToastUtil.diaplayMesLong(context, "申请退款成功");
					context.onSwipeRefreshLayoutRefresh();
				}
			}
			
			@Override
			public void onFinish() {
				progressDialog.dismiss();
			}
		});
	}
	
	private void toOrderDetailActivity(GetOrdersListResponse mOrdersList){
		Bundle mBundle = new Bundle();
		mBundle.putString(KeyUtil.OrderIdKey, mOrdersList.getOrderId());
		mBundle.putInt(KeyUtil.OrderTypeKey, mOrdersList.getOrderStatus());
		Intent intent = new Intent();
		intent.setClass(context, OrderDetailActivity.class);
		intent.putExtra(KeyUtil.BundleKey, mBundle);
		context.startActivity(intent);
	}
	
	static class ViewHolder1 {
		Button to_pay_btn;
		LinearLayout goods_list;
	}
	static class ViewHolder2 {
		LinearLayout goods_list;
	}
	static class ViewHolder3 {
		Button refund_btn;
		TextView order_code_img;
		LinearLayout goods_list;
	}
	static class ViewHolder4 {
		TextView order_refund_sum;
		LinearLayout goods_list;
	}
	static class ViewHolder5 {
		LinearLayout goods_list;
	}
	static class ViewHolder6 {
		LinearLayout goods_list;
	}

}

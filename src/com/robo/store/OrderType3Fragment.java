package com.robo.store;

import java.util.HashMap;
import java.util.List;

import org.apache.http.Header;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gc.materialdesign.views.ProgressBarCircularIndeterminate;
import com.gc.materialdesign.widgets.Dialog;
import com.robo.store.http.TextHttpResponseHandler;
import com.robo.store.dao.GetOrdersListResponse;
import com.robo.store.dao.GetSingleGoodsResponse;
import com.robo.store.dao.GetSingleOrderResponse;
import com.robo.store.dao.MallOrderDetailVO;
import com.robo.store.dao.OrderDetailVO;
import com.robo.store.dao.OrderGoods;
import com.robo.store.http.HttpParameter;
import com.robo.store.http.RoboHttpClient;
import com.robo.store.listener.onFragmentCallRefresh;
import com.robo.store.util.CartUtil;
import com.robo.store.util.KeyUtil;
import com.robo.store.util.LogUtil;
import com.robo.store.util.ResultParse;
import com.robo.store.util.ToastUtil;
import com.robo.store.util.ViewUtil;

public class OrderType3Fragment extends Fragment implements View.OnClickListener{

	private FrameLayout back_cover,quhuo_shop_cover;
	private TextView order_id_tv;
	private LinearLayout goods_list;
	private TextView order_pay_time_tv;
	private ImageView order_pay_method;
	private ImageView get_goods_code_img;
	private TextView get_goods_code;
	private TextView order_place_time_tv;
	private TextView order_refund_sum;
	private LinearLayout content_layout;
	private Button confirm_to_refund;
	private LayoutInflater inflater;
	
	protected View rootView;
	private onFragmentCallRefresh mRefreshListener;
	private GetSingleOrderResponse mSingleOrder;
	
	public OrderType3Fragment(GetSingleOrderResponse mSingleOrder){
		this.mSingleOrder = mSingleOrder;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override  
	  public void onAttach(Activity activity){  
	      super.onAttach(activity);  
	      try{  
	    	  mRefreshListener = (onFragmentCallRefresh) activity;  
	      }catch(Exception e){  
	          throw new ClassCastException(activity.toString()+"must implement OnArticleSelectedListener");  
	      }  
	  }  
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		this.inflater = inflater;
		rootView = inflater.inflate(R.layout.activity_order_type3, container, false);
		init();
		return rootView;
	}
	
	private void init(){
		back_cover = (FrameLayout) rootView.findViewById(R.id.back_cover);
		quhuo_shop_cover = (FrameLayout) rootView.findViewById(R.id.quhuo_shop_cover);
		order_id_tv = (TextView) rootView.findViewById(R.id.order_id_tv);
		goods_list = (LinearLayout) rootView.findViewById(R.id.goods_list);
		order_pay_time_tv = (TextView) rootView.findViewById(R.id.order_pay_time_tv);
		order_pay_method = (ImageView) rootView.findViewById(R.id.order_pay_method);
		get_goods_code_img = (ImageView) rootView.findViewById(R.id.get_goods_code_img);
		get_goods_code = (TextView) rootView.findViewById(R.id.get_goods_code);
		order_place_time_tv = (TextView) rootView.findViewById(R.id.order_place_time_tv);
		order_refund_sum = (TextView) rootView.findViewById(R.id.order_refund_sum);
		content_layout = (LinearLayout) rootView.findViewById(R.id.content_layout);
		confirm_to_refund = (Button) rootView.findViewById(R.id.confirm_to_refund);
		
		back_cover.setOnClickListener(this);
		quhuo_shop_cover.setOnClickListener(this);
		confirm_to_refund.setOnClickListener(this);
		
		setData(mSingleOrder);
	}
	
	private void setData(GetSingleOrderResponse mSingleOrder){
		order_id_tv.setText(mSingleOrder.getMallOrderCode());
		order_pay_time_tv.setText(mSingleOrder.getPayTime());
		if(mSingleOrder.getPayType() == 2){
			order_pay_method.setImageResource(R.drawable.pay_weixin_d);
		}else if(mSingleOrder.getPayType() == 1){
			order_pay_method.setImageResource(R.drawable.pay_zhifubao_d);
		}else{
			order_pay_method.setImageResource(R.drawable.bg_for_sale);
		}
		order_place_time_tv.setText("下单时间："+mSingleOrder.getOrderTime());
		List<MallOrderDetailVO> detailList = mSingleOrder.getDetailList();
		for(int i=0; i<detailList.size(); i++){
			MallOrderDetailVO mOrderDetailVO = detailList.get(i);
			if(i > 0){
				goods_list.addView( ViewUtil.getLine(getActivity(), R.color.text_grey) );
			}
			View goodsView = getGoodsView(mOrderDetailVO);
			LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			goodsView.setLayoutParams(mParams);
			goods_list.addView(goodsView);
		}
	}
	
	private View getGoodsView(final MallOrderDetailVO mOrderGoods){
		View goodsView = inflater.inflate(R.layout.order_list_goods_item, null);
		FrameLayout item_cover = (FrameLayout) goodsView.findViewById(R.id.item_cover);
		ImageView good_icon = (ImageView) goodsView.findViewById(R.id.good_icon);
		TextView good_name = (TextView) goodsView.findViewById(R.id.good_name);
		TextView good_price_new = (TextView) goodsView.findViewById(R.id.good_price_new);
		TextView goods_number = (TextView) goodsView.findViewById(R.id.goods_number);
		ImageView get_goods_shop = (ImageView) goodsView.findViewById(R.id.get_goods_shop);
		LinearLayout goods_refund_status_layout = (LinearLayout) goodsView.findViewById(R.id.goods_refund_status_layout);
		TextView goods_refund_status_tv = (TextView) goodsView.findViewById(R.id.goods_refund_status_tv);
		
//		Picasso.with(context)
//		.load(mOrderGoods.getGoodsPic())
//		.tag(context)
//		.into(good_icon);
		
		if(mOrderGoods.isPickUp()){
			goods_refund_status_layout.setVisibility(View.GONE);
		}else{
			goods_refund_status_layout.setVisibility(View.VISIBLE);
		}
		
		good_name.setText(mOrderGoods.getGoodsName());
		good_price_new.setText(mOrderGoods.getGoodsPrice());
		goods_number.setText("x"+mOrderGoods.getGoodsCount());
		item_cover.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				toGoodsDetailActivity(mOrderGoods.getGoodsBarcode());
			}
		});
		get_goods_shop.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ToastUtil.diaplayMesShort(getActivity(), "取货店铺信息");
			}
		});
		return goodsView;
	}
	
	private void toGoodsDetailActivity(String id){
		Intent intent = new Intent(getActivity(), GoodsDetailActivity.class);
		intent.putExtra(KeyUtil.GoodsIdKey, id);
		startActivity(intent);
	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.confirm_to_refund:
			refundOrder();
			break;
		case R.id.quhuo_shop_cover:
			RequestQuhuoShopData();
			break;
		case R.id.back_cover:
			getActivity().onBackPressed();
			break;
		}
	}
	
	private void refundOrder(){
		Dialog dialog = new Dialog(getActivity(), "温馨提示", "确定要申请退款吗？");
		dialog.addAcceptButton("确定");
		dialog.addCancelButton("取消");
		dialog.setOnAcceptButtonClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				RequestCancleData();
			}
		});
		dialog.setCancelable(true);
		dialog.show();
	}
	
	private void RequestCancleData(){
		final ProgressDialog progressDialog = ProgressDialog.show(getActivity(), "", "正在提交...", true, false);
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("orderId", mSingleOrder.getMallOrderCode());
		params.put("flag", "0");
		RoboHttpClient.get(HttpParameter.orderUrl, "refund", params, new TextHttpResponseHandler(){

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
				ToastUtil.diaplayMesLong(getActivity(), "连接失败，请重试！");
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, String result) {
				LogUtil.DefalutLog(result);
				mSingleOrder = (GetSingleOrderResponse) ResultParse.parseResult(result,GetSingleOrderResponse.class);
				if(ResultParse.handleResutl(getActivity(), mSingleOrder)){
					ToastUtil.diaplayMesLong(getActivity(), "申请退款成功");
					if(mRefreshListener != null){
						mRefreshListener.refresh();
					}
				}
			}
			
			@Override
			public void onFinish() {
				progressDialog.dismiss();
			}
		});
	}
	
	private void RequestQuhuoShopData(){
		final ProgressDialog progressDialog = ProgressDialog.show(getActivity(), "", "正在加载...", true, false);
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("orderCode", mSingleOrder.getMallOrderCode());
		RoboHttpClient.get(HttpParameter.orderUrl, "getPickupGoods", params, new TextHttpResponseHandler(){

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
				ToastUtil.diaplayMesLong(getActivity(), "连接失败，请重试！");
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, String result) {
				LogUtil.DefalutLog(result);
				mSingleOrder = (GetSingleOrderResponse) ResultParse.parseResult(result,GetSingleOrderResponse.class);
				if(ResultParse.handleResutl(getActivity(), mSingleOrder)){
					showQuhuoShop();
				}
			}
			
			@Override
			public void onFinish() {
				progressDialog.dismiss();
			}
		});
	}
	
	private void showQuhuoShop(){
		Dialog dialog = new Dialog(getActivity(), "取货记录", "取货记录");
		dialog.addAcceptButton("确定");
//		dialog.setOnAcceptButtonClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				RequestCancleData();
//			}
//		});
		dialog.setCancelable(true);
		dialog.show();
	}
	
	
}

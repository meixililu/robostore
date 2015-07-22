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
import com.squareup.picasso.Picasso;

public class OrderType6Fragment extends Fragment implements View.OnClickListener{

	private FrameLayout back_cover;
	private TextView order_id_tv;
	private LinearLayout goods_list;
	private TextView order_pay_time_tv;
	private ImageView order_pay_method;
	private TextView order_place_time_tv;
	private TextView order_sum;
	private LinearLayout content_layout;
	private Button confirm_to_refund;
	private LayoutInflater inflater;
	
	protected View rootView;
	private onFragmentCallRefresh mRefreshListener;
	private GetSingleOrderResponse mSingleOrder;
	
	public OrderType6Fragment(GetSingleOrderResponse mSingleOrder){
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
		rootView = inflater.inflate(R.layout.activity_order_type6, container, false);
		init();
		return rootView;
	}
	
	private void init(){
		back_cover = (FrameLayout) rootView.findViewById(R.id.back_cover);
		order_id_tv = (TextView) rootView.findViewById(R.id.order_id_tv);
		goods_list = (LinearLayout) rootView.findViewById(R.id.goods_list);
		order_pay_time_tv = (TextView) rootView.findViewById(R.id.order_pay_time_tv);
		order_pay_method = (ImageView) rootView.findViewById(R.id.order_pay_method);
		order_place_time_tv = (TextView) rootView.findViewById(R.id.order_place_time_tv);
		order_sum = (TextView) rootView.findViewById(R.id.order_sum);
		confirm_to_refund = (Button) rootView.findViewById(R.id.confirm_to_refund);
		content_layout = (LinearLayout) rootView.findViewById(R.id.content_layout);
		
		back_cover.setOnClickListener(this);
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
		order_sum.setText("共计：￥" + mSingleOrder.getTotalPrice());
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
		
		try {
			Picasso.with(getActivity())
			.load(mOrderGoods.getGoodsPic())
			.tag(getActivity())
			.into(good_icon);
		} catch (Exception e) {
			e.printStackTrace();
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
		case R.id.confirm_to_refund://删除订单
			cancleOrder();
			break;
		case R.id.back_cover:
			getActivity().onBackPressed();
			break;
		}
	}
	
	private void cancleOrder(){
		Dialog dialog = new Dialog(getActivity(), "温馨提示", "确定要删除该订单吗？");
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
		params.put("flag", "1");
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
					ToastUtil.diaplayMesLong(getActivity(), "订单已删除");
					CheckAllOrdersActivity.isNeedRefresh = true;
					getActivity().finish();
				}
			}
			
			@Override
			public void onFinish() {
				progressDialog.dismiss();
			}
		});
	}
	
}

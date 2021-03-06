package com.robo.store;

import java.util.HashMap;
import java.util.List;

import org.apache.http.Header;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gc.materialdesign.widgets.Dialog;
import com.robo.store.dao.GetPayParamsRespone;
import com.robo.store.dao.GetSingleOrderResponse;
import com.robo.store.dao.GetZfbPayParamsRespone;
import com.robo.store.dao.MallOrderDetailVO;
import com.robo.store.dialog.PayDialog;
import com.robo.store.http.HttpParameter;
import com.robo.store.http.RequestParams;
import com.robo.store.http.RoboHttpClient;
import com.robo.store.http.TextHttpResponseHandler;
import com.robo.store.listener.onFragmentCallRefresh;
import com.robo.store.listener.onPayTypeSelectedListener;
import com.robo.store.util.KeyUtil;
import com.robo.store.util.LogUtil;
import com.robo.store.util.PayResult;
import com.robo.store.util.ResultParse;
import com.robo.store.util.ToastUtil;
import com.robo.store.util.ViewUtil;
import com.robo.store.util.WechatPayUtil;
import com.robo.store.util.ZFBpayUtil;
import com.squareup.picasso.Picasso;

public class OrderType1Fragment extends Fragment implements View.OnClickListener{

	private FrameLayout back_cover;
	private TextView order_id_tv;
	private LinearLayout goods_list;
	private TextView sum_tv;
	private TextView order_place_time_tv;
	private FrameLayout order_cancle_cover;
	private Button confirm_to_pay;
	private LinearLayout content_layout;
	private LayoutInflater inflater;
	protected View rootView;
	
	private onFragmentCallRefresh mRefreshListener;
	private GetSingleOrderResponse mSingleOrder;
	
	public OrderType1Fragment(GetSingleOrderResponse mSingleOrder){
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
		rootView = inflater.inflate(R.layout.activity_order_type1, container, false);
		init();
		return rootView;
	}
	
	private void init(){
		back_cover = (FrameLayout) rootView.findViewById(R.id.back_cover);
		order_id_tv = (TextView) rootView.findViewById(R.id.order_id_tv);
		goods_list = (LinearLayout) rootView.findViewById(R.id.goods_list);
		sum_tv = (TextView) rootView.findViewById(R.id.sum_tv);
		order_cancle_cover = (FrameLayout) rootView.findViewById(R.id.order_cancle_cover);
		order_place_time_tv = (TextView) rootView.findViewById(R.id.order_place_time_tv);
		confirm_to_pay = (Button) rootView.findViewById(R.id.confirm_to_pay);
		content_layout = (LinearLayout) rootView.findViewById(R.id.content_layout);
		
		back_cover.setOnClickListener(this);
		confirm_to_pay.setOnClickListener(this);
		order_cancle_cover.setOnClickListener(this);
		
		setData(mSingleOrder);
	}
	
	private void setData(GetSingleOrderResponse mSingleOrder){
		order_id_tv.setText(mSingleOrder.getMallOrderCode());
		sum_tv.setText("￥"+mSingleOrder.getTotalPrice());
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
		
		try {
			Picasso.with(getActivity())
			.load(mOrderGoods.getGoodsPic())
			.tag(getActivity())
			.into(good_icon);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		goods_refund_status_layout.setVisibility(View.VISIBLE);
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
				toQuhuoShopActivity(mOrderGoods.getGoodsBarcode());
			}
		});
		return goodsView;
	}
	
	private void toQuhuoShopActivity(String goodsId){
		Bundle mBundle = new Bundle();
		mBundle.putString(KeyUtil.GoodsIdKey, goodsId);
		Intent intent = new Intent(getActivity(),KeQuHuoShopActivity.class);
		intent.putExtra(KeyUtil.BundleKey, mBundle);
		getActivity().startActivity(intent);
	}
	
	private void toGoodsDetailActivity(String id){
		Intent intent = new Intent(getActivity(), GoodsDetailActivity.class);
		intent.putExtra(KeyUtil.GoodsIdKey, id);
		startActivity(intent);
	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.confirm_to_pay:
			getPayType();
			break;
		case R.id.order_cancle_cover:
			cancleOrder();
			break;
		case R.id.back_cover:
			getActivity().onBackPressed();
			break;
		}
	}
	
	private void getPayType(){
		PayDialog mPayDialog = new PayDialog(getActivity());
		mPayDialog.setmListener(new onPayTypeSelectedListener() {
			@Override
			public void zfbPay() {
				toZFBPay();
			}
			@Override
			public void wechatPay() {
				toWechatPay();
			}
		});
		mPayDialog.show();
	}
	
	private void toWechatPay(){
		final ProgressDialog progressDialog = ProgressDialog.show(getActivity(), "", "正在调取支付...", true, false);
		RequestParams params = new RequestParams();
		params.put("orderId", mSingleOrder.getMallOrderCode());
		RoboHttpClient.postForPay(HttpParameter.payUrl, params, new TextHttpResponseHandler(){
			
			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
				ToastUtil.diaplayMesLong(getActivity(), getActivity().getResources().getString(R.string.connet_fail));
			}
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, String result) {
				GetPayParamsRespone mResponse = (GetPayParamsRespone) ResultParse.parseResult(result,GetPayParamsRespone.class);
				if(ResultParse.handleResutl(getActivity(), mResponse)){
					CheckAllOrdersActivity.isNeedRefresh = true;
					OrderDetailActivity.isNeedRefresh = true;
					WechatPayUtil.startWechat(mResponse.getPayParams());
				}
			}
			
			@Override
			public void onFinish() {
				progressDialog.dismiss();
			}
		});
	}
	
	private void toZFBPay(){
		final ProgressDialog progressDialog = ProgressDialog.show(getActivity(), "", "正在调取支付...", true, false);
		RequestParams params = new RequestParams();
		params.put("orderId", mSingleOrder.getMallOrderCode());
		RoboHttpClient.postForPay(HttpParameter.zfbUrl, params, new TextHttpResponseHandler(){
			
			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
				ToastUtil.diaplayMesLong(getActivity(), getActivity().getResources().getString(R.string.connet_fail));
			}
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, String result) {
				GetZfbPayParamsRespone mResponse = (GetZfbPayParamsRespone) ResultParse.parseResult(result,GetZfbPayParamsRespone.class);
				if(ResultParse.handleResutl(getActivity(), mResponse)){
					CheckAllOrdersActivity.isNeedRefresh = true;
					OrderDetailActivity.isNeedRefresh = true;
					ZFBpayUtil.startZFBpay(getActivity(), mHandler, mResponse.getAlipayParams());
				}
			}
			
			@Override
			public void onFinish() {
				progressDialog.dismiss();
			}
		});
	}
	
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case ZFBpayUtil.SDK_PAY_FLAG: {
				PayResult payResult = new PayResult((String) msg.obj);
				String resultStatus = payResult.getResultStatus();
				if (TextUtils.equals(resultStatus, "9000")) {
					if(mRefreshListener != null){
						CheckAllOrdersActivity.isNeedRefresh = true;
						mRefreshListener.refresh();
					}
					ToastUtil.diaplayMesShort(getActivity(), "支付成功");
				} else {
					if (TextUtils.equals(resultStatus, "8000")) {
						ToastUtil.diaplayMesShort(getActivity(), "支付结果确认中");
					} else {
						ToastUtil.diaplayMesShort(getActivity(), "支付失败");
					}
				}
				break;
			}
			default:
				break;
			}
		};
	};
	
	private void cancleOrder(){
		Dialog dialog = new Dialog(getActivity(), "温馨提示", "确定要取消该订单吗？");
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
		params.put("flag", "2");
		RoboHttpClient.post(HttpParameter.orderUrl, "refund", params, new TextHttpResponseHandler(){

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
				ToastUtil.diaplayMesLong(getActivity(), "连接失败，请重试！");
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, String result) {
				LogUtil.DefalutLog(result);
				mSingleOrder = (GetSingleOrderResponse) ResultParse.parseResult(result,GetSingleOrderResponse.class);
				if(ResultParse.handleResutl(getActivity(), mSingleOrder)){
					ToastUtil.diaplayMesLong(getActivity(), "订单已取消");
					if(mRefreshListener != null){
						CheckAllOrdersActivity.isNeedRefresh = true;
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
	
	
}

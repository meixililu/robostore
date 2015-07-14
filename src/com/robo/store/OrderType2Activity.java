package com.robo.store;

import java.util.HashMap;
import java.util.List;

import org.apache.http.Header;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gc.materialdesign.views.ProgressBarCircularIndeterminate;
import com.robo.store.http.TextHttpResponseHandler;
import com.robo.store.dao.GetOrdersListResponse;
import com.robo.store.dao.GetSingleGoodsResponse;
import com.robo.store.dao.GetSingleOrderResponse;
import com.robo.store.dao.MallOrderDetailVO;
import com.robo.store.dao.OrderDetailVO;
import com.robo.store.dao.OrderGoods;
import com.robo.store.http.HttpParameter;
import com.robo.store.http.RoboHttpClient;
import com.robo.store.util.CartUtil;
import com.robo.store.util.KeyUtil;
import com.robo.store.util.LogUtil;
import com.robo.store.util.ResultParse;
import com.robo.store.util.ToastUtil;
import com.robo.store.util.ViewUtil;

public class OrderType2Activity extends BaseActivity implements View.OnClickListener{

	private TextView order_id_tv;
	private LinearLayout goods_list;
	private TextView order_place_time_tv;
	private TextView order_sum;
	private Button confirm_to_pay;
	private LinearLayout content_layout;
	private LayoutInflater inflater;
	
	private String mallOrderId;
	private GetSingleOrderResponse mSingleOrder;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_type2);
		init();
		RequestData();
	}
	
	private void init(){
		Bundle mBundle = getIntent().getBundleExtra(KeyUtil.BundleKey);
		if(mBundle != null){
			mallOrderId = mBundle.getString(KeyUtil.OrderIdKey);
		}
		inflater = LayoutInflater.from(this);
		order_id_tv = (TextView) findViewById(R.id.order_id_tv);
		goods_list = (LinearLayout) findViewById(R.id.goods_list);
		order_sum = (TextView) findViewById(R.id.order_sum);
		order_place_time_tv = (TextView) findViewById(R.id.order_place_time_tv);
		confirm_to_pay = (Button) findViewById(R.id.confirm_to_pay);
		content_layout = (LinearLayout) findViewById(R.id.content_layout);
		
		confirm_to_pay.setOnClickListener(this);
	}
	
	private void setData(GetSingleOrderResponse mSingleOrder){
		order_id_tv.setText(mSingleOrder.getMallOrderCode());
		order_sum.setText("共计：￥"+mSingleOrder.getMallOrderCode());
		order_place_time_tv.setText(mSingleOrder.getMallOrderCode());
		List<MallOrderDetailVO> detailList = mSingleOrder.getDetailList();
		for(int i=0; i<detailList.size(); i++){
			MallOrderDetailVO mOrderDetailVO = detailList.get(i);
			if(i > 0){
				goods_list.addView( ViewUtil.getLine(this, R.color.text_grey) );
			}
			View goodsView = getGoodsView(mOrderDetailVO);
			LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			goodsView.setLayoutParams(mParams);
			goods_list.addView(goodsView);
		}
	}
	
	private void RequestData(){
		mProgressbar.setVisibility(View.VISIBLE);
		content_layout.setVisibility(View.GONE);
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("mallOrderId", mallOrderId);
		RoboHttpClient.get(HttpParameter.orderUrl, "getSingleOrder", params, new TextHttpResponseHandler(){

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
				ToastUtil.diaplayMesLong(OrderType2Activity.this, "连接失败，请重试！");
				showEmptyLayout_Error();
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, String result) {
				LogUtil.DefalutLog(result);
				mSingleOrder = (GetSingleOrderResponse) ResultParse.parseResult(result,GetSingleOrderResponse.class);
				if(ResultParse.handleResutl(OrderType2Activity.this, mSingleOrder)){
					content_layout.setVisibility(View.VISIBLE);
					setData(mSingleOrder);
				}else{
					showEmptyLayout_Empty();
				}
			}
			
			@Override
			public void onFinish() {
				mProgressbar.setVisibility(View.GONE);
			}
		});
	}
	
	private View getGoodsView(final MallOrderDetailVO mOrderGoods){
		View goodsView = inflater.inflate(R.layout.order_list_goods_item, null);
		FrameLayout item_cover = (FrameLayout) goodsView.findViewById(R.id.item_cover);
		ImageView good_icon = (ImageView) goodsView.findViewById(R.id.good_icon);
		TextView good_name = (TextView) goodsView.findViewById(R.id.good_name);
		TextView good_price_new = (TextView) goodsView.findViewById(R.id.good_price_new);
		TextView goods_number = (TextView) goodsView.findViewById(R.id.goods_number);
		TextView already_get_goods_tv = (TextView) goodsView.findViewById(R.id.already_get_goods_tv);
		ImageView get_goods_shop = (ImageView) goodsView.findViewById(R.id.get_goods_shop);
		LinearLayout goods_refund_status_layout = (LinearLayout) goodsView.findViewById(R.id.goods_refund_status_layout);
		TextView goods_refund_status_tv = (TextView) goodsView.findViewById(R.id.goods_refund_status_tv);
		
//		Picasso.with(context)
//		.load(mOrderGoods.getGoodsPic())
//		.tag(context)
//		.into(good_icon);
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
				ToastUtil.diaplayMesShort(OrderType2Activity.this, "取货店铺信息");
			}
		});
		return goodsView;
	}
	
	private void toGoodsDetailActivity(String id){
		Intent intent = new Intent(this, GoodsDetailActivity.class);
		intent.putExtra(KeyUtil.GoodsIdKey, id);
		startActivity(intent);
	}
	
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch(v.getId()){
		case R.id.confirm_to_pay:
			
			break;
		}
	}
	
	
}
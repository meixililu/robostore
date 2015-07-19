package com.robo.store;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.Header;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.robo.store.adapter.ConfirmToPayListViewAdapter;
import com.robo.store.dao.AddOrderDetailVO;
import com.robo.store.dao.AddOrderResponse;
import com.robo.store.dao.GoodsBase;
import com.robo.store.http.HttpParameter;
import com.robo.store.http.RoboHttpClient;
import com.robo.store.http.TextHttpResponseHandler;
import com.robo.store.util.CartUtil;
import com.robo.store.util.KeyUtil;
import com.robo.store.util.NumberUtil;
import com.robo.store.util.ResultParse;
import com.robo.store.util.ToastUtil;

public class ConfirmOrderActivity extends BaseActivity implements OnClickListener{

	private ListView content_lv;
	private RadioButton rb_weixin,rb_zhifubao;
	private TextView total_tv;
	private Button confirm_to_pay;
	
	private List<GoodsBase> confirmList;
	private LayoutInflater inflater;
	private ConfirmToPayListViewAdapter mAdapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(getResources().getString(R.string.confirm_order));
		setContentView(R.layout.activity_confirm_order);
		init();
	}
	
	private void init(){
		inflater = LayoutInflater.from(this);
		confirmList = new ArrayList<GoodsBase>();
		content_lv = (ListView) findViewById(R.id.content_lv);
		rb_weixin = (RadioButton) findViewById(R.id.rb_weixin);
		rb_zhifubao = (RadioButton) findViewById(R.id.rb_zhifubao);
		total_tv = (TextView) findViewById(R.id.total_tv);
		confirm_to_pay = (Button) findViewById(R.id.confirm_to_pay);
		total_tv.setText("合计：￥"+ CartFragment.totalSum);
		CartUtil.addToConfirmList(confirmList);
		mAdapter = new ConfirmToPayListViewAdapter(this, inflater, confirmList, total_tv);
		content_lv.setAdapter(mAdapter);
		confirm_to_pay.setOnClickListener(this);
	}
	
	private void toPay(){
		if(rb_weixin.isChecked()){
			submitOrders();
		}else if(rb_zhifubao.isChecked()){
			
		}else{
			ToastUtil.diaplayMesShort(this, "请选择支付方式");
		}
	}
	
	private void submitOrders(){
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("cityId", HomeFragment.cityId);
		params.put("totalPrice", CartFragment.totalSum);
		params.put("list", getGoodsList());
		RoboHttpClient.get(HttpParameter.orderUrl,"addOrder", params, new TextHttpResponseHandler(){
			
			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
				ToastUtil.diaplayMesLong(ConfirmOrderActivity.this, ConfirmOrderActivity.this.getResources().getString(R.string.connet_fail));
			}
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, String result) {
				AddOrderResponse mResponse = (AddOrderResponse) ResultParse.parseResult(result,AddOrderResponse.class);
				if(ResultParse.handleResutl(ConfirmOrderActivity.this, mResponse)){
					ToastUtil.diaplayMesLong(ConfirmOrderActivity.this, "订单提交成功");
					Bundle mBundle = new Bundle();
					mBundle.putString(KeyUtil.OrderIdKey, mResponse.getOrderId());
					Intent intent = new Intent();
					intent.setClass(ConfirmOrderActivity.this, OrderType3Fragment.class);
					intent.putExtra(KeyUtil.BundleKey, mBundle);
					ConfirmOrderActivity.this.startActivity(intent);
				}
			}
			
			@Override
			public void onFinish() {
			}
		});
	}
	
	private List<AddOrderDetailVO> getGoodsList(){
		List<AddOrderDetailVO> mGoodsList = new ArrayList<AddOrderDetailVO>();
		for(GoodsBase mBase : confirmList){
			AddOrderDetailVO mAddOrderDetailVO = new AddOrderDetailVO();
			mAddOrderDetailVO.setGoodsBarcode(mBase.getGoodsBarcode());
			mAddOrderDetailVO.setGoodsCount(mBase.getNumber());
			mAddOrderDetailVO.setGoodsPrice( NumberUtil.StringToDouble(mBase.getVipPrice()) );
			mGoodsList.add(mAddOrderDetailVO);
		}
		return mGoodsList;
	}
	
	
	private void paySuccee(){
		CartFragment.cartList.removeAll(confirmList);
	}
	
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch(v.getId()){
		case R.id.confirm_to_pay:
			toPay();
			break;
		}
	}
	
}

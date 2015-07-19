package com.robo.store;

import java.util.HashMap;

import org.apache.http.Header;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.LinearLayout;

import com.robo.store.dao.GetSingleOrderResponse;
import com.robo.store.http.HttpParameter;
import com.robo.store.http.RoboHttpClient;
import com.robo.store.http.TextHttpResponseHandler;
import com.robo.store.listener.onFragmentCallRefresh;
import com.robo.store.util.KeyUtil;
import com.robo.store.util.LogUtil;
import com.robo.store.util.ResultParse;
import com.robo.store.util.ToastUtil;

public class OrderDetailActivity extends BaseActivity implements onFragmentCallRefresh{

	private LinearLayout parent_layout;
	private String mallOrderId;
	private int orderStatus;
	private GetSingleOrderResponse mSingleOrder;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_orderdetail);
		init();
		RequestData();
	}
	
	private void init(){
		Bundle mBundle = getIntent().getBundleExtra(KeyUtil.BundleKey);
		if(mBundle != null){
			mallOrderId = mBundle.getString(KeyUtil.OrderIdKey);
			orderStatus = mBundle.getInt(KeyUtil.OrderTypeKey);
		}
		parent_layout = (LinearLayout) findViewById(R.id.parent_layout);
	}
	
	private void RequestData(){
		mProgressbar.setVisibility(View.VISIBLE);
		parent_layout.setVisibility(View.GONE);
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("mallOrderId", mallOrderId);
		RoboHttpClient.get(HttpParameter.orderUrl, "getSingleOrder", params, new TextHttpResponseHandler(){

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
				ToastUtil.diaplayMesLong(OrderDetailActivity.this, "连接失败，请重试！");
				showEmptyLayout_Error();
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, String result) {
				LogUtil.DefalutLog(result);
				mSingleOrder = (GetSingleOrderResponse) ResultParse.parseResult(result,GetSingleOrderResponse.class);
				if(ResultParse.handleResutl(OrderDetailActivity.this, mSingleOrder)){
					parent_layout.setVisibility(View.VISIBLE);
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
	
	private void setData(GetSingleOrderResponse mSingleOrder){
		orderStatus = mSingleOrder.getOrderType();
		FragmentManager fragmentManager = this.getSupportFragmentManager();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		Fragment mFragment = null;
		if(orderStatus == 1){
			mFragment = new OrderType1Fragment(mSingleOrder);
		}else if(orderStatus == 2){
			mFragment = new OrderType2Fragment(mSingleOrder);
		}else if(orderStatus == 3){
			mFragment = new OrderType3Fragment(mSingleOrder);
		}else if(orderStatus == 4){
			mFragment = new OrderType4Fragment(mSingleOrder);
		}else if(orderStatus == 5){
			mFragment = new OrderType5Fragment(mSingleOrder);
		}else if(orderStatus == 6){
			mFragment = new OrderType6Fragment(mSingleOrder);
		}
		transaction.replace(R.id.parent_layout,mFragment);
		transaction.commit();
	}

	@Override
	public void refresh() {
		RequestData();
	}
	
	public void onClickEmptyLayoutRefresh(){
		RequestData();
	}
}

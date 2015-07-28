package com.robo.store;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.Header;

import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.robo.store.adapter.PickupRecordListAdapter;
import com.robo.store.dao.GetPickUpLogResponse;
import com.robo.store.dao.PickUpLog;
import com.robo.store.http.HttpParameter;
import com.robo.store.http.RoboHttpClient;
import com.robo.store.http.TextHttpResponseHandler;
import com.robo.store.util.KeyUtil;
import com.robo.store.util.ResultParse;
import com.robo.store.util.Settings;
import com.robo.store.util.ToastUtil;

public class PickupRecordListActivity extends BaseActivity implements OnClickListener{

	private LayoutInflater inflater;
	private ListView mListView;
	private PickupRecordListAdapter mCashOrderListAdapter;
	private List<PickUpLog> list;
	
	private String orderCode;
	private boolean isFinishloadData = true;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pickup_record_list);
		init();
		RequestData();
	}
	
	private void init(){
		Bundle mBundle = getIntent().getBundleExtra(KeyUtil.BundleKey);
		if(mBundle != null){
			orderCode = mBundle.getString(KeyUtil.OrderIdKey);
		}
		inflater = LayoutInflater.from(this);
		list = new ArrayList<PickUpLog>();
		mCashOrderListAdapter = new PickupRecordListAdapter(this, inflater, list);
		initSwipeRefresh();
		mListView = (ListView) findViewById(R.id.content_lv);
		mListView.setAdapter(mCashOrderListAdapter);
	}
	
	
	public void onSwipeRefreshLayoutRefresh(){
		clearList();
		RequestData();
	}
	
	public void clearList(){
		list.clear();
		mCashOrderListAdapter.notifyDataSetChanged();
	}
	
	private void RequestData(){
		if(isFinishloadData){
			hideEmptyLayout();
			isFinishloadData = false;
			showProgressbar();
			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("orderCode", orderCode);
			RoboHttpClient.get(HttpParameter.orderUrl,"getPickUpLog", params, new TextHttpResponseHandler(){
				
				@Override
				public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
					ToastUtil.diaplayMesLong(PickupRecordListActivity.this, PickupRecordListActivity.this.getResources().getString(R.string.connet_fail));
				}
				
				@Override
				public void onSuccess(int arg0, Header[] arg1, String result) {
					GetPickUpLogResponse mResponse = (GetPickUpLogResponse) ResultParse.parseResult(result,GetPickUpLogResponse.class);
					if(ResultParse.handleResutl(PickupRecordListActivity.this, mResponse)){
//						PickUpLog mPickUpLog = new PickUpLog();
//						mPickUpLog.setMachineName("售货机1");
//						mPickUpLog.setPickUpTime("2015-07-28 15:14:30");
//						mPickUpLog.setShopName("天猫龙眼店");
//						list.add(mPickUpLog);
						list.addAll(mResponse.getDetailList());
						mCashOrderListAdapter.notifyDataSetChanged();
						if(list.size() < 1){
							showEmptyLayout_Empty();
						}
					}else{
						showEmptyLayout_Empty();
					}
				}
				
				@Override
				public void onFinish() {
					isFinishloadData = true;
					onSwipeRefreshLayoutFinish();
					hideProgressbar();
				}
			});
		}
	}
	
	@Override
	public void onClickEmptyLayoutRefresh() {
		onSwipeRefreshLayoutRefresh();
	}
	
	@Override
	public void onClick(View v) {
		super.onClick(v);
	}
	
}

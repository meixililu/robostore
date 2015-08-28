package com.robo.store;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.Header;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

import com.robo.store.adapter.PickupRecordListAdapter;
import com.robo.store.dao.GetPickUpLogResponse;
import com.robo.store.dao.GetPickUpLogVO;
import com.robo.store.http.HttpParameter;
import com.robo.store.http.RoboHttpClient;
import com.robo.store.http.TextHttpResponseHandler;
import com.robo.store.util.KeyUtil;
import com.robo.store.util.ResultParse;
import com.robo.store.util.ToastUtil;

public class PickupRecordListActivity extends BaseActivity implements OnClickListener{

	private LayoutInflater inflater;
	private ListView mListView;
	private PickupRecordListAdapter mCashOrderListAdapter;
	private List<GetPickUpLogVO> list;
	
	private String orderCode;
	private boolean isFinishloadData = true;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pickup_record_list);
		init();
		mSwipeRefreshLayout.setRefreshing(true);
		RequestData();
	}
	
	private void init(){
		Bundle mBundle = getIntent().getBundleExtra(KeyUtil.BundleKey);
		if(mBundle != null){
			orderCode = mBundle.getString(KeyUtil.OrderIdKey);
		}
		inflater = LayoutInflater.from(this);
		list = new ArrayList<GetPickUpLogVO>();
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
			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("orderId", orderCode);
			RoboHttpClient.post(HttpParameter.orderUrl,"getPickUpLog", params, new TextHttpResponseHandler(){
				
				@Override
				public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
					ToastUtil.diaplayMesLong(PickupRecordListActivity.this, PickupRecordListActivity.this.getResources().getString(R.string.connet_fail));
				}
				
				@Override
				public void onSuccess(int arg0, Header[] arg1, String result) {
					GetPickUpLogResponse mResponse = (GetPickUpLogResponse) ResultParse.parseResult(result,GetPickUpLogResponse.class);
					if(ResultParse.handleResutl(PickupRecordListActivity.this, mResponse)){
						list.addAll(mResponse.getList());
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
		mSwipeRefreshLayout.setRefreshing(true);
		onSwipeRefreshLayoutRefresh();
	}
	
	@Override
	public void onClick(View v) {
		super.onClick(v);
	}
	
}

package com.robo.store;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.Header;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.robo.store.adapter.CheckAllOrderListAdapter;
import com.robo.store.dao.GetAllOrderResponse;
import com.robo.store.dao.GetOrdersListResponse;
import com.robo.store.dao.OrderGoods;
import com.robo.store.http.HttpParameter;
import com.robo.store.http.RoboHttpClient;
import com.robo.store.http.TextHttpResponseHandler;
import com.robo.store.util.KeyUtil;
import com.robo.store.util.LogUtil;
import com.robo.store.util.ResultParse;
import com.robo.store.util.Settings;
import com.robo.store.util.ToastUtil;

public class CheckAllOrdersActivity extends BaseActivity implements OnClickListener{

	//（订单状态：“1.待付款”，“2.订单取消”，“3.待取货”，“4.退款处理中”，“5.已退款，交易关闭”，“6.交易完成”6种状态）
	private SwipeRefreshLayout mswiperefreshlayout;
	private LinearLayout empty_layout;
	private ListView mListView;
	private View footerView;
	private LinearLayout load_more_data;
	private TextView no_more_data,empty_prompt_tv;
	private LayoutInflater inflater;
	
	private CheckAllOrderListAdapter mCheckAllOrderListAdapter;
	private List<GetOrdersListResponse> ordersList;
	
	private int OrderType;
	public int pageIndex;
	private boolean isLoadMoreData;
	private boolean isFinishloadData;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle();
		setContentView(R.layout.activity_check_all_orders);
		init();
		RequestData();
	}
	
	private void setTitle(){
		Bundle mBundle = getIntent().getBundleExtra(KeyUtil.BundleKey);
		if(mBundle != null){
			OrderType = mBundle.getInt(KeyUtil.OrderTypeKey,0);
		}
		if(OrderType == 0){
			setTitle(getResources().getString(R.string.check_all_orders));
		}else if(OrderType == 1){
			setTitle(getResources().getString(R.string.obligantion));
		}else if(OrderType == 3){
			setTitle(getResources().getString(R.string.to_pick_up));
		}else if(OrderType == 4){
			setTitle(getResources().getString(R.string.refunding_list));
		}
	}
	
	private void init(){
		inflater = LayoutInflater.from(this);
		initSwipeRefresh();
		empty_layout = (LinearLayout) findViewById(R.id.empty_layout);
		mListView = (ListView) findViewById(R.id.content_lv);
		empty_prompt_tv = (TextView) findViewById(R.id.empty_prompt_tv);
		footerView = inflater.inflate(R.layout.list_footer_view, null);
		load_more_data = (LinearLayout) footerView.findViewById(R.id.load_more_data);
		no_more_data = (TextView) footerView.findViewById(R.id.no_more_data);
		footerView.setVisibility(View.GONE);
		mListView.addFooterView(footerView);
		empty_layout.setOnClickListener(this);
		setListOnScrollListener();
		ordersList = new ArrayList<GetOrdersListResponse>();
		mCheckAllOrderListAdapter = new CheckAllOrderListAdapter(this, inflater, ordersList);
		mListView.setAdapter(mCheckAllOrderListAdapter);
	}
	
	public void setListOnScrollListener(){
		mListView.setOnScrollListener(new OnScrollListener() {  
            private int lastItemIndex;
            @Override  
            public void onScrollStateChanged(AbsListView view, int scrollState) { 
                if (scrollState == OnScrollListener.SCROLL_STATE_IDLE && lastItemIndex == mCheckAllOrderListAdapter.getCount() - 1) {  
                	LogUtil.DefalutLog("onScrollStateChanged---update");
                	if(isFinishloadData){
                		if(isLoadMoreData){
                			RequestData();
                		}
                	}
                }  
            }  
            @Override  
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {  
                lastItemIndex = firstVisibleItem + visibleItemCount - 2;  
            }  
        });
	}
	
	public void onSwipeRefreshLayoutRefresh(){
		clearList();
		RequestData();
	}
	
	public void clearList(){
		pageIndex = 0;
		footerView.setVisibility(View.GONE);
		load_more_data.setVisibility(View.VISIBLE);
		no_more_data.setVisibility(View.GONE);
		ordersList.clear();
		mCheckAllOrderListAdapter.notifyDataSetChanged();
	}
	
	private void RequestData(){
		isFinishloadData = false;
		if(pageIndex == 0){
			mProgressbar.setVisibility(View.VISIBLE);
		}
		empty_layout.setVisibility(View.GONE);
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("type", OrderType);
		params.put("pageIndex", pageIndex);
		params.put("pageCount", Settings.pageCount);
		RoboHttpClient.get(HttpParameter.orderUrl,"getOrdersList", params, new TextHttpResponseHandler(){

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
				ToastUtil.diaplayMesLong(CheckAllOrdersActivity.this, CheckAllOrdersActivity.this.getResources().getString(R.string.connet_fail));
			}
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, String result) {
				GetAllOrderResponse mListResponse = (GetAllOrderResponse) ResultParse.parseResult(result,GetAllOrderResponse.class);
				if(ResultParse.handleResutl(CheckAllOrdersActivity.this, mListResponse)){
					List<GetOrdersListResponse> mOrderList = mListResponse.getOrderList();
					if(mOrderList.size() > 0){
//						for(int i=0;i<10;i++){
							ordersList.addAll(mOrderList);
//						}
						mCheckAllOrderListAdapter.notifyDataSetChanged();
						if(mOrderList.size() < Settings.pageCount && pageIndex == 0){
							isLoadMoreData = false;
							mListView.removeFooterView(footerView);
						}else{
							isLoadMoreData = true;
							footerView.setVisibility(View.VISIBLE);
							pageIndex++;
						}
					}else{
						empty_layout.setVisibility(View.VISIBLE);
						empty_prompt_tv.setText(CheckAllOrdersActivity.this.getResources().getString(R.string.order_list_empty));
						isLoadMoreData = false;
						load_more_data.setVisibility(View.GONE);
						no_more_data.setVisibility(View.VISIBLE);
					}
				}else{
					mListView.removeFooterView(footerView);
				}
			}
			
			@Override
			public void onFinish() {
				isFinishloadData = true;
				mSwipeRefreshLayout.setRefreshing(false);
				mProgressbar.setVisibility(View.GONE);
			}
		});
	}
	
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch(v.getId()){
		case R.id.empty_layout:
			
			break;
		}
	}
	
	
}

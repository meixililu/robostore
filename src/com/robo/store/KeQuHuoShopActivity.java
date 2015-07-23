package com.robo.store;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.Header;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.gc.materialdesign.views.ProgressBarCircularIndeterminate;
import com.robo.store.adapter.QuhuoShopPinnedListAdapter;
import com.robo.store.dao.GetShopListByGoodsList;
import com.robo.store.dao.GetShopListByGoodsResponse;
import com.robo.store.dao.GetShopListByGoodsVO;
import com.robo.store.dao.ShopBase;
import com.robo.store.http.HttpParameter;
import com.robo.store.http.RoboHttpClient;
import com.robo.store.http.TextHttpResponseHandler;
import com.robo.store.util.KeyUtil;
import com.robo.store.util.LogUtil;
import com.robo.store.util.ResultParse;
import com.robo.store.util.Settings;
import com.robo.store.util.ToastUtil;
import com.robo.store.view.PinnedSectionListView;

public class KeQuHuoShopActivity extends BaseActivity implements OnClickListener{
	
	private LayoutInflater inflater;
	private PinnedSectionListView pinnedlist;
	private View footerView;
	private LinearLayout load_more_data;
	private TextView no_more_data;
	private TextView empty_layout;
	
	private LocationClient mLocationClient;
	private BDLocationListener myListener;
	public int pageIndex = 0;
	private boolean isLoadMoreData;
	private boolean isFinishloadData = true;
	
	private String goodsBarcode;
	
	
	private List<GetShopListByGoodsVO> AllDataList;
	private QuhuoShopPinnedListAdapter mShopListAdapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_quhuo_shop);
		initView();
		initLocation();
		QueryShopByArea();
	}
	
	private void initLocation(){
		if(ShopFragment.latitude <= 0){
			mLocationClient = new LocationClient(this);
			myListener = new MyLocationListener();
			mLocationClient.registerLocationListener( myListener );
			InitLocation();
			mLocationClient.start();
		}
	}

	protected void initView(){
		Bundle mBundle = getIntent().getBundleExtra(KeyUtil.BundleKey);
		if(mBundle != null){
			goodsBarcode = mBundle.getString(KeyUtil.GoodsIdKey);
		}
		this.inflater = LayoutInflater.from(this);
		AllDataList = new ArrayList<GetShopListByGoodsVO>();
		mShopListAdapter = new QuhuoShopPinnedListAdapter(this, inflater, AllDataList);
		pinnedlist = (PinnedSectionListView) findViewById(R.id.pinnedlist);
		empty_layout = (TextView) findViewById(R.id.empty_layout);
		initSwipeRefresh();
		setListOnScrollListener();
		
		footerView = inflater.inflate(R.layout.list_footer_view, null);
		footerView.setVisibility(View.GONE);
		pinnedlist.addFooterView(footerView);
		load_more_data = (LinearLayout) footerView.findViewById(R.id.load_more_data);
		no_more_data = (TextView) footerView.findViewById(R.id.no_more_data);
		
		pinnedlist.setAdapter(mShopListAdapter);
	}
	
	public void setListOnScrollListener(){
		pinnedlist.setOnScrollListener(new OnScrollListener() {  
            private int lastItemIndex;//当前ListView中最后一个Item的索引  
            @Override  
            public void onScrollStateChanged(AbsListView view, int scrollState) { 
                if (scrollState == OnScrollListener.SCROLL_STATE_IDLE && lastItemIndex == mShopListAdapter.getCount() - 1) {  
                	LogUtil.DefalutLog("onScrollStateChanged---update");
            		if(isLoadMoreData){
            			QueryShopByArea();
            		}
                }  
            }  
            @Override  
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {  
                lastItemIndex = firstVisibleItem + visibleItemCount - 2;  
            }  
        });
	}
	
	private void InitLocation(){
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Battery_Saving);//设置定位模式
		option.setCoorType("gcj02");//返回的定位结果是百度经纬度，默认值gcj02
		option.setScanSpan(5000);//设置发起定位请求的间隔时间为5000ms
		option.setIsNeedAddress(true);
		mLocationClient.setLocOption(option);
	}

	public void onSwipeRefreshLayoutRefresh(){
		clearList();
		QueryShopByArea();
	}
	
	public void clearList(){
		pageIndex = 0;
		AllDataList.clear();
		footerView.setVisibility(View.GONE);
		mShopListAdapter.notifyDataSetChanged();
		load_more_data.setVisibility(View.VISIBLE);
		no_more_data.setVisibility(View.GONE);
	}
	
	private void isEmpty(){
		if(pageIndex == 0){
			if(AllDataList.size() == 0){
				empty_layout.setVisibility(View.VISIBLE);
			}
		}
	}
	
	private void QueryShopByArea(){
		if(isFinishloadData){
			isFinishloadData = false;
			if(pageIndex == 0){
				showProgressbar();
			}
			empty_layout.setVisibility(View.GONE);
			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("cityId", HomeFragment.cityId);
			params.put("goodsBarcode", goodsBarcode);
			params.put("longitude", ShopFragment.longitude);
			params.put("latitude", ShopFragment.latitude);
			params.put("pageIndex", pageIndex);
			params.put("pageCount", Settings.pageCount);
			RoboHttpClient.get(HttpParameter.shopsUrl,"getShopListByGoods", params, new TextHttpResponseHandler(){
				
				@Override
				public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
					ToastUtil.diaplayMesLong(KeQuHuoShopActivity.this, KeQuHuoShopActivity.this.getResources().getString(R.string.connet_fail));
				}
				
				@Override
				public void onSuccess(int arg0, Header[] arg1, String result) {
					GetShopListByGoodsResponse mResponse = (GetShopListByGoodsResponse) ResultParse.parseResult(result,GetShopListByGoodsResponse.class);
					if(ResultParse.handleResutl(KeQuHuoShopActivity.this, mResponse)){
						List<GetShopListByGoodsList> mList = mResponse.getList();
						if(mList.size() > 0){
							for (GetShopListByGoodsList mBean : mList) {
								GetShopListByGoodsVO mGetShopListByGoodsVO = getSectionBean(mBean.getAreaLevelName());
								AllDataList.add(mGetShopListByGoodsVO);
								AllDataList.addAll(mBean.getList());
							}
							if(mList.size() < Settings.pageCount && pageIndex == 0){
								isLoadMoreData = false;
								pinnedlist.removeFooterView(footerView);
							}else{
								isLoadMoreData = true;
								pageIndex++;
								footerView.setVisibility(View.VISIBLE);
							}
						}else{
							isLoadMoreData = false;
							load_more_data.setVisibility(View.GONE);
							no_more_data.setVisibility(View.VISIBLE);
						}
						mShopListAdapter.notifyDataSetChanged();
					}
				}
				
				@Override
				public void onFinish() {
					isFinishloadData = true;
					onSwipeRefreshLayoutFinish();
					hideProgressbar();
					isEmpty();
				}
			});
		}
	}
	
	public class MyLocationListener implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null) return ;
			ShopFragment.longitude = location.getLongitude();
			ShopFragment.latitude = location.getLatitude();
			LogUtil.DefalutLog("KeQuHuoShopActivity---longitude:"+ShopFragment.longitude+"---latitude:"+ShopFragment.latitude);
		}
	}
	
	public void onClickEmptyLayoutRefresh(){
		onSwipeRefreshLayoutRefresh();
	}
	
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch(v.getId()){
		case R.id.search_cover:
			Bundle mBundle = new Bundle();
			mBundle.putString(KeyUtil.SearchTypeKey, SearchActivity.SearchShops);	
			toActivity(SearchActivity.class, mBundle);
			break;
		}
	}
	
	private GetShopListByGoodsVO getSectionBean(String name){
		GetShopListByGoodsVO mShopBase = new GetShopListByGoodsVO();
		mShopBase.setShopName(name);
		mShopBase.setType(1);
		return mShopBase;
	}

	@Override
	protected void onStop() {
		super.onStop();
		if(mLocationClient != null){
			mLocationClient.stop();
		}
	}
}

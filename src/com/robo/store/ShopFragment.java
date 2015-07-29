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
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.FrameLayout;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.gc.materialdesign.views.ProgressBarCircularIndeterminate;
import com.robo.store.adapter.ShopPinnedListAdapter;
import com.robo.store.dao.GetShopListResponse;
import com.robo.store.dao.ShopBase;
import com.robo.store.http.HttpParameter;
import com.robo.store.http.RoboHttpClient;
import com.robo.store.http.TextHttpResponseHandler;
import com.robo.store.util.KeyUtil;
import com.robo.store.util.LogUtil;
import com.robo.store.util.LoginUtil;
import com.robo.store.util.ResultParse;
import com.robo.store.util.Settings;
import com.robo.store.util.ToastUtil;
import com.robo.store.view.PinnedSectionListView;

public class ShopFragment extends BaseFragment implements OnClickListener{
	
	private LayoutInflater inflater;
	private ProgressBarCircularIndeterminate mProgressbar;
	public SwipeRefreshLayout mSwipeRefreshLayout;
	private FrameLayout search_cover;
	private PinnedSectionListView pinnedlist;
	private View footerView;
	private LinearLayout load_more_data;
	private TextView no_more_data;
	private TextView empty_layout;
	
	private LocationClient mLocationClient = null;
	private BDLocationListener myListener = new MyLocationListener();
	public static double longitude,latitude;
	public int pageIndex = 0;
	private boolean isLoadMoreData;
	private boolean isFinishloadData = true;
	private boolean isNearByListAdd;
	private int isBeforeUseListAdd;
	private int isNearbyListAdd;
	private int loadCount;
	
	private List<ShopBase> AllDataList;
	private ShopPinnedListAdapter mShopListAdapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mLocationClient = new LocationClient(getActivity());
		mLocationClient.registerLocationListener( myListener );
		InitLocation();
		mLocationClient.start();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		LogUtil.DefalutLog(ShopFragment.class.getName()+"---onCreateView");
		this.inflater = inflater;
		setLayoutId(R.layout.fragment_shop);
        return super.onCreateView(inflater, container, savedInstanceState);
	}
	
	protected void initView(){
		AllDataList = new ArrayList<ShopBase>();
		mShopListAdapter = new ShopPinnedListAdapter(getActivity(), inflater, AllDataList);
		search_cover = (FrameLayout) getView().findViewById(R.id.search_cover);
		pinnedlist = (PinnedSectionListView) getView().findViewById(R.id.pinnedlist);
		mProgressbar = (ProgressBarCircularIndeterminate) getView().findViewById(R.id.progressbar_m);
		empty_layout = (TextView) getView().findViewById(R.id.empty_layout);
		mSwipeRefreshLayout = (SwipeRefreshLayout) getView().findViewById(R.id.mswiperefreshlayout);
		mSwipeRefreshLayout.setColorSchemeResources(R.color.holo_blue_bright, 
	            R.color.holo_green_light, 
	            R.color.holo_orange_light, 
	            R.color.holo_red_light);
		mSwipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				onSwipeRefreshLayoutRefresh();
			}
		});
		setListOnScrollListener();
		
		footerView = inflater.inflate(R.layout.list_footer_view, null);
		footerView.setVisibility(View.GONE);
		pinnedlist.addFooterView(footerView);
		load_more_data = (LinearLayout) footerView.findViewById(R.id.load_more_data);
		no_more_data = (TextView) footerView.findViewById(R.id.no_more_data);
		
		pinnedlist.setAdapter(mShopListAdapter);
		empty_layout.setOnClickListener(this);
		search_cover.setOnClickListener(this);
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

	protected void loadData(){
		if(LoginUtil.isLogin){
			QueryShopByNearby();
			QueryShopByBeforeUse();
		}
		QueryShopByArea();
	}
	
	public void onSwipeRefreshLayoutRefresh(){
		clearList();
		loadData();
	}
	
	public void clearList(){
		loadCount = 0;
		pageIndex = 0;
		isBeforeUseListAdd = 0;
		isNearbyListAdd = 0;
		AllDataList.clear();
		footerView.setVisibility(View.GONE);
		mShopListAdapter.notifyDataSetChanged();
		load_more_data.setVisibility(View.VISIBLE);
		no_more_data.setVisibility(View.GONE);
	}
	
	private void isEmpty(){
		if(pageIndex == 0){
			if(loadCount == 3){
				if(AllDataList.size() == 0){
					empty_layout.setVisibility(View.VISIBLE);
				}
			}
		}
	}
	
	private void QueryShopByArea(){
		if(isFinishloadData){
			isFinishloadData = false;
			if(pageIndex == 0){
				mProgressbar.setVisibility(View.VISIBLE);
			}
			empty_layout.setVisibility(View.GONE);
			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("longitude", longitude);
			params.put("latitude", latitude);
			params.put("cityId", HomeFragment.cityId);
			params.put("pageIndex", pageIndex);
			params.put("pageCount", Settings.pageCount);
			RoboHttpClient.post(HttpParameter.shopsUrl,"queryShopByArea", params, new TextHttpResponseHandler(){
				
				@Override
				public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
					ToastUtil.diaplayMesLong(getActivity(), getActivity().getResources().getString(R.string.connet_fail));
				}
				
				@Override
				public void onSuccess(int arg0, Header[] arg1, String result) {
					GetShopListResponse mResponse = (GetShopListResponse) ResultParse.parseResult(result,GetShopListResponse.class);
					if(ResultParse.handleResutl(getActivity(), mResponse)){
						List<ShopBase> mList = mResponse.getList();
						if(mList.size() > 0){
							if(pageIndex == 0){
								mList.add(0, getSectionBean(HomeFragment.city+"内店铺"));
							}
							AllDataList.addAll(mList);
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
					loadCount++;
					isFinishloadData = true;
					mSwipeRefreshLayout.setRefreshing(false);
					mProgressbar.setVisibility(View.GONE);
					isEmpty();
				}
			});
		}
	}
	
	private void QueryShopByBeforeUse(){
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("longitude", longitude);
		params.put("latitude", latitude);
		RoboHttpClient.post(HttpParameter.shopsUrl,"queryShopByBeforeUse", params, new TextHttpResponseHandler(){

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, String result) {
				GetShopListResponse mResponse = (GetShopListResponse) ResultParse.parseResult(result,GetShopListResponse.class);
				if(ResultParse.handleResutl(getActivity(), mResponse)){
					List<ShopBase> mList = mResponse.getList();
					isBeforeUseListAdd = mList.size();
					if(isBeforeUseListAdd > 0){
						mList.add(0, getSectionBean("最近使用的店铺"));
						AllDataList.addAll(0,mList);
						mShopListAdapter.notifyDataSetChanged();
					}
				}
			}
			@Override
			public void onFinish() {
				loadCount++;
				isEmpty();
			}
		});
	}
	
	private void QueryShopByNearby(){
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("longitude", longitude);
		params.put("latitude", latitude);
		params.put("cityId", HomeFragment.cityId);
		RoboHttpClient.post(HttpParameter.shopsUrl,"queryShopByNearby", params, new TextHttpResponseHandler(){

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
				ToastUtil.diaplayMesLong(getActivity(), getActivity().getResources().getString(R.string.connet_fail));
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, String result) {
				GetShopListResponse mResponse = (GetShopListResponse) ResultParse.parseResult(result,GetShopListResponse.class);
				if(ResultParse.handleResutl(getActivity(), mResponse)){
					List<ShopBase> mList = mResponse.getList();
					isNearbyListAdd = mList.size(); 
					if(isNearbyListAdd > 0){
						mList.add(0, getSectionBean("附近的店铺"));
						AllDataList.addAll(isBeforeUseListAdd, mList);
						mShopListAdapter.notifyDataSetChanged();
					}
				}
			}
			@Override
			public void onFinish() {
				loadCount++;
				isEmpty();
			}
		});
	}
	
	public class MyLocationListener implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null) return ;
			longitude = location.getLongitude();
			latitude = location.getLatitude();
			LogUtil.DefalutLog("---longitude:"+longitude+"---latitude:"+latitude);
		}
	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.search_cover:
			Bundle mBundle = new Bundle();
			mBundle.putString(KeyUtil.SearchTypeKey, SearchActivity.SearchShops);	
			toActivity(SearchActivity.class, mBundle);
			break;
		case R.id.empty_layout:
			onSwipeRefreshLayoutRefresh();
			break;
		}
	}
	
	private ShopBase getSectionBean(String name){
		ShopBase mShopBase = new ShopBase();
		mShopBase.setShopName(name);
		mShopBase.setType(1);
		return mShopBase;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		if(mLocationClient != null){
			mLocationClient.stop();
		}
		LogUtil.DefalutLog("ShopFragment---onDestroyView");
	}
}

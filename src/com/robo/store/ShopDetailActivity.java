package com.robo.store;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.apache.http.Header;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;

import com.robo.store.adapter.CheckAllOrderListAdapter;
import com.robo.store.adapter.HomeListViewAdapter;
import com.robo.store.adapter.HomeMenuGridViewAdapter;
import com.robo.store.dao.GetGoodsListResponse;
import com.robo.store.dao.GetOrdersListResponse;
import com.robo.store.dao.GoodsBase;
import com.robo.store.dao.GoodsType;
import com.robo.store.http.HttpParameter;
import com.robo.store.http.RoboHttpClient;
import com.robo.store.http.TextHttpResponseHandler;
import com.robo.store.util.HomeUtil;
import com.robo.store.util.KeyUtil;
import com.robo.store.util.LogUtil;
import com.robo.store.util.ResultParse;
import com.robo.store.util.Settings;
import com.robo.store.util.ToastUtil;
import com.robo.store.view.AutoScrollViewPager;
import com.robo.store.view.MyGridView;

public class ShopDetailActivity extends BaseActivity implements OnClickListener{

	private LayoutInflater inflater;
	private ListView mListView;
	private FrameLayout search_cover;
	private Button check_shop_location_btn;
	private HomeMenuGridViewAdapter mMenuAdapter;
	private List<GoodsType> mGoodsTypeList;
	public SwipeRefreshLayout mSwipeRefreshLayout;
	private HomeListViewAdapter mHomeListViewAdapter;
	private List<GoodsBase> goodsList;
	
	private View headerView,footerView;
	private LinearLayout load_more_data;
	private TextView no_more_data;
	private MyGridView mGridView;
	private String goodType = "0";
	public int pageIndex = 0;
	private boolean isLoadMoreData;
	private boolean isFinishloadData;
	private String shopId;
	
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
			shopId = mBundle.getString(KeyUtil.ShopDetailIdKey);
			String title = mBundle.getString(KeyUtil.ShopDetailTitleKey);
			setTitle(title);
		}
		
	}
	
	private void init(){
		inflater = LayoutInflater.from(this);
		mGoodsTypeList = new ArrayList<GoodsType>();
		goodsList = new ArrayList<GoodsBase>();
		initSwipeRefresh();
		search_cover = (FrameLayout) findViewById(R.id.search_cover);
		mListView = (ListView) findViewById(R.id.content_lv);
		check_shop_location_btn = (Button) findViewById(R.id.check_shop_location_btn);
		
		footerView = inflater.inflate(R.layout.list_footer_view, null);
		load_more_data = (LinearLayout) footerView.findViewById(R.id.load_more_data);
		no_more_data = (TextView) footerView.findViewById(R.id.no_more_data);
		footerView.setVisibility(View.GONE);
		mListView.addFooterView(footerView);
		
		headerView = inflater.inflate(R.layout.home_list_header, null);
		mGridView = (MyGridView) headerView.findViewById(R.id.gridview);
		mGridView.setAdapter(mMenuAdapter);
		mGridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				GoodsType mGoodsType = mGoodsTypeList.get(position);
				goodType = mGoodsType.getGoodsTypeId();
				HomeUtil.setSelectedMenu(mGoodsTypeList, goodType);
				mMenuAdapter.notifyDataSetChanged();
				clearList();
				RequestData();
			}
		});
		mHomeListViewAdapter = new HomeListViewAdapter(this, inflater, goodsList);
		mMenuAdapter = new HomeMenuGridViewAdapter(this, inflater, mGoodsTypeList);
		setListOnScrollListener();
		mListView.setAdapter(mHomeListViewAdapter);
		
		search_cover.setOnClickListener(this);
		check_shop_location_btn.setOnClickListener(this);
	}
	
	public void setListOnScrollListener(){
		mListView.setOnScrollListener(new OnScrollListener() {  
            private int lastItemIndex;
            @Override  
            public void onScrollStateChanged(AbsListView view, int scrollState) { 
                if (scrollState == OnScrollListener.SCROLL_STATE_IDLE && lastItemIndex == mHomeListViewAdapter.getCount() - 1) {  
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
		goodsList.clear();
		footerView.setVisibility(View.GONE);
		mHomeListViewAdapter.notifyDataSetChanged();
		load_more_data.setVisibility(View.VISIBLE);
		no_more_data.setVisibility(View.GONE);
	}
	
	private void RequestData(){
		isFinishloadData = false;
		if(pageIndex == 0){
			mProgressbar.setVisibility(View.VISIBLE);
		}
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("cityId", HomeFragment.cityId);
		params.put("type", goodType);
		params.put("pageIndex", pageIndex);
		params.put("pageCount", Settings.pageCount);
		RoboHttpClient.get(HttpParameter.goodUrl,"getGoodsListByType", params, new TextHttpResponseHandler(){

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
				ToastUtil.diaplayMesLong(ShopDetailActivity.this, ShopDetailActivity.this.getResources().getString(R.string.connet_fail));
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, String result) {
				GetGoodsListResponse mGetGoodsListResponse = (GetGoodsListResponse) ResultParse.parseResult(result,GetGoodsListResponse.class);
				if(ResultParse.handleResutl(ShopDetailActivity.this, mGetGoodsListResponse)){
					List<GoodsType> typeList = mGetGoodsListResponse.getTypeList();
					Collections.reverse(typeList);
					mGoodsTypeList.clear();
					mGoodsTypeList.addAll(typeList);
					HomeUtil.setSelectedMenu(mGoodsTypeList, goodType);
					
					List<GoodsBase> mGoodsList = mGetGoodsListResponse.getGoodsList();
					goodsList.addAll(mGoodsList);
					
					mMenuAdapter.notifyDataSetChanged();
					mHomeListViewAdapter.notifyDataSetChanged();
					if(mGoodsList.size() > 0){
						isLoadMoreData = true;
						pageIndex++;
						footerView.setVisibility(View.VISIBLE);
					}else{
						isLoadMoreData = false;
						load_more_data.setVisibility(View.GONE);
						no_more_data.setVisibility(View.VISIBLE);
					}
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
		switch(v.getId()){
		case R.id.search_cover:
			Bundle mBundle = new Bundle();
			mBundle.putString(KeyUtil.SearchTypeKey, SearchActivity.SearchShops);	
			mBundle.putString(KeyUtil.ShopDetailIdKey, shopId);	
			toActivity(SearchActivity.class, mBundle);
			break;
		case R.id.check_shop_location_btn:
			
			break;
		}
	}
}

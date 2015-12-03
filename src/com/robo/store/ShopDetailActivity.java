package com.robo.store;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.apache.http.Header;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.robo.store.adapter.HomeListViewAdapter;
import com.robo.store.adapter.HomeMenuGridViewAdapter;
import com.robo.store.dao.GetShopInfoResponse;
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
import com.robo.store.view.MyGridView;

public class ShopDetailActivity extends BaseActivity implements OnClickListener{

	private LayoutInflater inflater;
	private ListView mListView;
	private FrameLayout search_cover;
	private Button check_shop_location_btn;
	private HomeMenuGridViewAdapter mMenuAdapter;
	private List<GoodsType> mGoodsTypeList;
	private HomeListViewAdapter mHomeListViewAdapter;
	private List<GoodsBase> goodsList;
	private GetShopInfoResponse mGetGoodsListResponse;
	
	private View headerView,footerView;
	private LinearLayout load_more_data;
	private TextView no_more_data;
	private MyGridView mGridView;
	private String goodType = "0";
	public int pageIndex = 0;
	private boolean isLoadMoreData;
	private boolean isFinishloadData = true;
	private String shopId;
	private boolean isInitFooterView;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle();
		setContentView(R.layout.activity_shop_detail);
		init();
		onCreateShowProgressbar();
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
		mHomeListViewAdapter = new HomeListViewAdapter(this, inflater, goodsList);
		mMenuAdapter = new HomeMenuGridViewAdapter(this, inflater, mGoodsTypeList);
		initSwipeRefresh();
		search_cover = (FrameLayout) findViewById(R.id.search_cover);
		mListView = (ListView) findViewById(R.id.content_lv);
		check_shop_location_btn = (Button) findViewById(R.id.check_shop_location_btn);
		
		footerView = inflater.inflate(R.layout.list_footer_view, null);
		load_more_data = (LinearLayout) footerView.findViewById(R.id.load_more_data);
		no_more_data = (TextView) footerView.findViewById(R.id.no_more_data);
		footerView.setVisibility(View.GONE);
		
		headerView = inflater.inflate(R.layout.shop_list_header, null);
		mGridView = (MyGridView) headerView.findViewById(R.id.gridview);
		mGridView.setAdapter(mMenuAdapter);
		mGridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				if(mGoodsTypeList.size() > 8 && position == 7 && !mMenuAdapter.isShowAll()){
					mMenuAdapter.setShowAll(true);
					mMenuAdapter.notifyDataSetChanged();
				}else{
					GoodsType mGoodsType = mGoodsTypeList.get(position);
					goodType = mGoodsType.getGoodsTypeId();
					HomeUtil.setSelectedMenu(mGoodsTypeList, goodType);
					mMenuAdapter.notifyDataSetChanged();
					clearList();
					mSwipeRefreshLayout.setRefreshing(true);
					RequestData();
				}
			}
		});
		setListOnScrollListener();
		mListView.addHeaderView(headerView);
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
            		if(isLoadMoreData){
            			RequestData();
            		}
                }  
            }  
            @Override  
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {  
                lastItemIndex = firstVisibleItem + visibleItemCount - 3;  
            }  
        });
	}
	
	public void onSwipeRefreshLayoutRefresh(){
		clearList();
		RequestData();
	}
	
	@Override
	public void onClickEmptyLayoutRefresh() {
		mSwipeRefreshLayout.setRefreshing(true);
		onSwipeRefreshLayoutRefresh();
	}
	
	public void clearList(){
		pageIndex = 0;
		goodsList.clear();
		isInitFooterView = false;
		footerView.setVisibility(View.GONE);
		mHomeListViewAdapter.notifyDataSetChanged();
		load_more_data.setVisibility(View.VISIBLE);
		no_more_data.setVisibility(View.GONE);
		mListView.removeFooterView(footerView);
	}
	
	private void RequestData(){
		if(isFinishloadData){
			mGridView.setEnabled(false);
			isFinishloadData = false;
			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("shopId", shopId);
			params.put("type", goodType);
			params.put("pageIndex", pageIndex);
			params.put("pageCount", Settings.pageCount);
			RoboHttpClient.post(HttpParameter.goodUrl,"getShopInfo", params, new TextHttpResponseHandler(){
				
				@Override
				public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
					showEmptyLayout_Error();
					ToastUtil.diaplayMesLong(ShopDetailActivity.this, ShopDetailActivity.this.getResources().getString(R.string.connet_fail));
				}
				
				@Override
				public void onSuccess(int arg0, Header[] arg1, String result) {
					mGetGoodsListResponse = (GetShopInfoResponse) ResultParse.parseResult(result,GetShopInfoResponse.class);
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
							if(mGoodsList.size() < Settings.pageCount && pageIndex == 0){
								isLoadMoreData = false;
							}else{
								initFooterView();
								footerView.setVisibility(View.VISIBLE);
								isLoadMoreData = true;
								pageIndex++;
							}
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
					mGridView.setEnabled(true);
				}
			});
		}
	}
	
	private void initFooterView(){
		if(!isInitFooterView){
			isInitFooterView = true;
			mListView.addFooterView(footerView);
		}
	}
	
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch(v.getId()){
		case R.id.search_cover:
			toSearchActivity();
			break;
		case R.id.check_shop_location_btn:
			toMapActivity();
			break;
		}
	}
	
	private void toSearchActivity(){
		Bundle mBundle = new Bundle();
		mBundle.putString(KeyUtil.SearchTypeKey, SearchActivity.SearchGoods);	
		mBundle.putString(KeyUtil.ShopDetailIdKey, shopId);	
		toActivity(SearchActivity.class, mBundle);
	}
	
	private void toMapActivity(){
		if(!TextUtils.isEmpty(mGetGoodsListResponse.getLatitude()) && !TextUtils.isEmpty(mGetGoodsListResponse.getLongitude())){
			Bundle mBundle = new Bundle();
			mBundle.putString(KeyUtil.LatitudeKey, mGetGoodsListResponse.getLatitude());	
			mBundle.putString(KeyUtil.LongitudeKey, mGetGoodsListResponse.getLongitude());	
			mBundle.putString(KeyUtil.ShopMemoKey, mGetGoodsListResponse.getShopMemo());	
			toActivity(ShopLocationActivity.class, mBundle);
		}
	}
}

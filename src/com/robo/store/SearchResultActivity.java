package com.robo.store;

import java.util.ArrayList;
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
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.robo.store.adapter.HomeListViewAdapter;
import com.robo.store.adapter.ShopSearchListAdapter;
import com.robo.store.dao.GetGoodsByNameInShopResponse;
import com.robo.store.dao.GetShopListResponse;
import com.robo.store.dao.GoodsBase;
import com.robo.store.dao.ShopBase;
import com.robo.store.http.HttpParameter;
import com.robo.store.http.RoboHttpClient;
import com.robo.store.http.TextHttpResponseHandler;
import com.robo.store.util.KeyUtil;
import com.robo.store.util.LogUtil;
import com.robo.store.util.ResultParse;
import com.robo.store.util.Settings;
import com.robo.store.util.ToastUtil;
import com.robo.store.util.UnicodeToStr;

public class SearchResultActivity extends BaseActivity implements OnClickListener{

	private LinearLayout list_layout,message_layout;
	private ListView mListView;
	private TextView result_number_tv;
	private View footerView;
	private LinearLayout load_more_data;
	private TextView no_more_data;
	private LayoutInflater inflater;
	
	private HomeListViewAdapter mHomeListViewAdapter;
	private ShopSearchListAdapter mShopSearchListAdapter;
	private List<GoodsBase> goodsList;
	private List<ShopBase> shopList;
	
	private String searchContent;
	private String searchType;
	private String shopId;
	public int pageIndex;
	private boolean isLoadMoreData;
	private boolean isFinishloadData = true;
	
	private FrameLayout search_cover;
	private EditText search_et;
	private String hintText;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle();
		setContentView(R.layout.activity_search_result);
		init();
		RequestData();
	}
	
	private void setTitle(){
		String title = "";
		Bundle mBundle = getIntent().getBundleExtra(KeyUtil.BundleKey);
		if(mBundle != null){
			searchType = mBundle.getString(KeyUtil.SearchTypeKey);
			searchContent = mBundle.getString(KeyUtil.SearchContentKey);
			shopId = mBundle.getString(KeyUtil.ShopDetailIdKey);
		}
//		if(searchType.equals(SearchActivity.SearchGoods)){
//			title = this.getResources().getString(R.string.search_goods_result);
//		}else{
//			title = this.getResources().getString(R.string.search_shops_result);
//		}
//		setTitle(title);
	}
	
	private void init(){
		inflater = LayoutInflater.from(this);
		initSwipeRefresh();
		list_layout = (LinearLayout) findViewById(R.id.list_layout);
		message_layout = (LinearLayout) findViewById(R.id.message_layout);
		mListView = (ListView) findViewById(R.id.content_lv);
		result_number_tv = (TextView) findViewById(R.id.result_number_tv);
		
		if(searchType.equals(SearchActivity.SearchGoods)){
			hintText = "请输入商品名称";
		}else{
			hintText = "请输入店铺名称";
		}
		search_cover = (FrameLayout) findViewById(R.id.search_cover);
		search_et = (EditText) findViewById(R.id.search_tv);
		search_et.setHint(hintText);
		search_cover.setOnClickListener(this);
		
		footerView = inflater.inflate(R.layout.list_footer_view, null);
		load_more_data = (LinearLayout) footerView.findViewById(R.id.load_more_data);
		no_more_data = (TextView) footerView.findViewById(R.id.no_more_data);
		footerView.setVisibility(View.GONE);
		mListView.addFooterView(footerView);
		message_layout.setOnClickListener(this);
		setListOnScrollListener();
		if(searchType.equals(SearchActivity.SearchGoods)){
			goodsList = new ArrayList<GoodsBase>();
			mHomeListViewAdapter = new HomeListViewAdapter(this, inflater, goodsList);
			mListView.setAdapter(mHomeListViewAdapter);
		}else{
			shopList = new ArrayList<ShopBase>();
			mShopSearchListAdapter = new ShopSearchListAdapter(this, inflater, shopList);
			mListView.setAdapter(mShopSearchListAdapter);
			
		}
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
		if(searchType.equals(SearchActivity.SearchGoods)){
			goodsList.clear();
			mHomeListViewAdapter.notifyDataSetChanged();
		}else{
			shopList.clear();
			mShopSearchListAdapter.notifyDataSetChanged();
		}
	}
	
	private void RequestData(){
		if(isFinishloadData){
			isFinishloadData = false;
			if(pageIndex == 0){
				list_layout.setVisibility(View.GONE);
			}
			message_layout.setVisibility(View.GONE);
			if(searchType.equals(SearchActivity.SearchGoods)){
				HashMap<String, Object> params = new HashMap<String, Object>();
				params.put("goodsName", UnicodeToStr.toUnicode(searchContent));
				params.put("cityId", HomeFragment.cityId);
				params.put("shopId", shopId);
				params.put("pageIndex", pageIndex);
				params.put("pageCount", Settings.pageCount);
				RoboHttpClient.post(HttpParameter.goodUrl,"getGoodsByName", params, new TextHttpResponseHandler(){
					
					@Override
					public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
						ToastUtil.diaplayMesLong(SearchResultActivity.this, SearchResultActivity.this.getResources().getString(R.string.connet_fail));
					}
					
					@Override
					public void onSuccess(int arg0, Header[] arg1, String result) {
						GetGoodsByNameInShopResponse mListResponse = (GetGoodsByNameInShopResponse) ResultParse.parseResult(result,GetGoodsByNameInShopResponse.class);
						if(ResultParse.handleResutl(SearchResultActivity.this, mListResponse)){
							List<GoodsBase> mGoodsList = mListResponse.getGoodsList();
							if(mGoodsList.size() > 0){
								goodsList.addAll(mGoodsList);
								mHomeListViewAdapter.notifyDataSetChanged();
								result_number_tv.setText("共" + mGoodsList.size() + "条结果");
								list_layout.setVisibility(View.VISIBLE);
								if(mGoodsList.size() < Settings.pageCount && pageIndex == 0){
									isLoadMoreData = false;
									mListView.removeFooterView(footerView);
								}else{
									isLoadMoreData = true;
									footerView.setVisibility(View.VISIBLE);
									pageIndex++;
								}
							}else{
								message_layout.setVisibility(View.VISIBLE);
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
			}else{
				HashMap<String, Object> params = new HashMap<String, Object>();
				params.put("shopName", UnicodeToStr.toUnicode(searchContent));
				params.put("cityId", HomeFragment.cityId);
				params.put("longitude", ShopFragment.longitude);
				params.put("latitude", ShopFragment.latitude);
				params.put("pageIndex", pageIndex);
				params.put("pageCount", Settings.pageCount);
				RoboHttpClient.post(HttpParameter.shopsUrl,"getShopListByName", params, new TextHttpResponseHandler(){
					
					@Override
					public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
						ToastUtil.diaplayMesLong(SearchResultActivity.this, SearchResultActivity.this.getResources().getString(R.string.connet_fail));
					}
					
					@Override
					public void onSuccess(int arg0, Header[] arg1, String result) {
						GetShopListResponse mListResponse = (GetShopListResponse) ResultParse.parseResult(result,GetShopListResponse.class);
						if(ResultParse.handleResutl(SearchResultActivity.this, mListResponse)){
							List<ShopBase> mShopList = mListResponse.getList();
							if(mShopList.size() > 0){
								shopList.addAll(mShopList);
								mShopSearchListAdapter.notifyDataSetChanged();
								result_number_tv.setText("共" + mShopList.size() + "条结果");
								list_layout.setVisibility(View.VISIBLE);
								if(mShopList.size() < Settings.pageCount && pageIndex == 0){
									isLoadMoreData = false;
									mListView.removeFooterView(footerView);
								}else{
									isLoadMoreData = true;
									footerView.setVisibility(View.VISIBLE);
									pageIndex++;
								}
							}else{
								message_layout.setVisibility(View.VISIBLE);
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
		}
	}
	
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch(v.getId()){
		case R.id.empty_layout:
			SearchResultActivity.this.finish();
			break;
		case R.id.search_cover:
			search();
			
			break;
		}
	}
	
	private boolean validData(){
		boolean isvalid = true;
		String temp = search_et.getText().toString().trim();
		if(TextUtils.isEmpty(temp)){
			ToastUtil.diaplayMesShort(this, hintText);
			isvalid = false;
		}else{
			searchContent = temp;
		}
		return isvalid;
	}
	
	private void search(){
		if(validData()){
			mSwipeRefreshLayout.setRefreshing(true);
			onSwipeRefreshLayoutRefresh();
		}
	}
	
	
}

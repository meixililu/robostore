package com.robo.store;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.apache.http.Header;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.gc.materialdesign.views.ProgressBarCircularIndeterminate;
import com.robo.store.http.TextHttpResponseHandler;
import com.robo.store.adapter.HomeListViewAdapter;
import com.robo.store.adapter.HomeMenuGridViewAdapter;
import com.robo.store.adapter.ImagePagerAdapter;
import com.robo.store.dao.GetGoodsListResponse;
import com.robo.store.dao.GoodsBase;
import com.robo.store.dao.GoodsType;
import com.robo.store.http.HttpParameter;
import com.robo.store.http.RoboHttpClient;
import com.robo.store.util.HomeUtil;
import com.robo.store.util.KeyUtil;
import com.robo.store.util.LogUtil;
import com.robo.store.util.ResultParse;
import com.robo.store.util.SPUtil;
import com.robo.store.util.Settings;
import com.robo.store.util.ToastUtil;
import com.robo.store.util.ViewUtil;
import com.robo.store.view.AutoScrollViewPager;
import com.robo.store.view.MyGridView;

public class HomeFragment extends BaseFragment implements OnClickListener{

	public static int RequestCity = 1000;
	private TextView city_tv;
	private TextView search_btn;
	private FrameLayout city_cover;
	private SharedPreferences mSharedPreferences;
	private ListView mListView;
	private LinearLayout viewpager_dot_layout;
	private ProgressBarCircularIndeterminate mProgressbar;
	private LayoutInflater inflater;
	private HomeMenuGridViewAdapter mMenuAdapter;
	private List<GoodsType> mGoodsTypeList;
	public SwipeRefreshLayout mSwipeRefreshLayout;
	
	private HomeListViewAdapter mHomeListViewAdapter;
	private List<GoodsBase> goodsList;
	private boolean isHashFinishInitView;
	private TextView empty_layout;
	
	private View headerView,footerView;
	private LinearLayout load_more_data;
	private TextView no_more_data;
	private MyGridView mGridView;
	private AutoScrollViewPager auto_view_pager;
	private String goodType = "0";
	public static String city;
	public static String cityId;
	public static BaseFragment mBaseFragment;
	public int pageIndex = 0;
	private boolean isLoadMoreData;
	private boolean isFinishloadData = true;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mBaseFragment = this;
		mSharedPreferences = SPUtil.getSharedPreferences(getActivity());
		city = mSharedPreferences.getString(KeyUtil.CityKey, "");
		cityId = mSharedPreferences.getString(KeyUtil.CityIdKey, "");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		LogUtil.DefalutLog(HomeFragment.class.getName()+"---onCreateView");
		this.inflater = inflater;
		setLayoutId(R.layout.fragment_home);
        return super.onCreateView(inflater, container, savedInstanceState);
	}
	
	protected void initView(){
		city_cover = (FrameLayout) getView().findViewById(R.id.title_cover);
		city_tv = (TextView) getView().findViewById(R.id.title);
		search_btn = (TextView) getView().findViewById(R.id.search_btn);
		mListView = (ListView) getView().findViewById(R.id.content_lv);
		empty_layout = (TextView) getView().findViewById(R.id.empty_layout);
		mProgressbar = (ProgressBarCircularIndeterminate) getView().findViewById(R.id.progressbar_m);
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
		
		mGoodsTypeList = new ArrayList<GoodsType>();
		goodsList = new ArrayList<GoodsBase>();
		
		mHomeListViewAdapter = new HomeListViewAdapter(getActivity(), inflater, goodsList);
		mMenuAdapter = new HomeMenuGridViewAdapter(getActivity(), inflater, mGoodsTypeList);
		
		footerView = inflater.inflate(R.layout.list_footer_view, null);
		no_more_data = (TextView) footerView.findViewById(R.id.no_more_data);
		load_more_data = (LinearLayout) footerView.findViewById(R.id.load_more_data);
		footerView.setVisibility(View.GONE);
		mListView.addFooterView(footerView);
		
		headerView = inflater.inflate(R.layout.home_list_header, null);
		viewpager_dot_layout = (LinearLayout) headerView.findViewById(R.id.viewpager_dot_layout);
		mGridView = (MyGridView) headerView.findViewById(R.id.gridview);
		auto_view_pager = (AutoScrollViewPager) headerView.findViewById(R.id.auto_view_pager);
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
		
		ArrayList<Integer> imageIdList = new ArrayList<Integer>();
        imageIdList.add(R.drawable.banner1);
        imageIdList.add(R.drawable.banner2);
        imageIdList.add(R.drawable.banner3);
        imageIdList.add(R.drawable.banner4);
        imageIdList.add(R.drawable.banner5);
        auto_view_pager.setAdapter(new ImagePagerAdapter(getActivity(), imageIdList).setInfiniteLoop(true));
        auto_view_pager.setOnPageChangeListener(new MyOnPageChangeListener());
        auto_view_pager.setInterval(2000);
        auto_view_pager.startAutoScroll();
        int currentItem = Integer.MAX_VALUE / 2 - Integer.MAX_VALUE / 2 % imageIdList.size();
        auto_view_pager.setCurrentItem(currentItem);
        for(int i=0;i<imageIdList.size();i++){
        	viewpager_dot_layout.addView( ViewUtil.getDot(getActivity(),i) );
        }
        ViewUtil.changeState(viewpager_dot_layout, currentItem%5);
        
        setListOnScrollListener();
        mListView.addHeaderView(headerView);
		mListView.setAdapter(mHomeListViewAdapter);
		
		empty_layout.setOnClickListener(this);
		city_cover.setOnClickListener(this);
		search_btn.setOnClickListener(this);
		if(!TextUtils.isEmpty(city) && !TextUtils.isEmpty(cityId)){
			city_tv.setText(city);
		}else{
			toActivityForResult(CityActivity.class, null, RequestCity);
		}
		isHashFinishInitView = true;
		loadData();
	}
	
	public void setListOnScrollListener(){
		mListView.setOnScrollListener(new OnScrollListener() {  
            private int lastItemIndex;//当前ListView中最后一个Item的索引  
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
	
	public void clearList(){
		pageIndex = 0;
		goodsList.clear();
		footerView.setVisibility(View.GONE);
		mHomeListViewAdapter.notifyDataSetChanged();
		load_more_data.setVisibility(View.VISIBLE);
		no_more_data.setVisibility(View.GONE);
	}
	
	protected void loadData(){
		LogUtil.DefalutLog("HomeFragment---setUserVisibleHint---loadData");
		if(isHashFinishInitView){
			isHashFinishInitView = false;
			RequestData();
		}
	}
	
	private void isEmpty(){
		if(pageIndex == 0){
			if(goodsList.size() == 0 && mGoodsTypeList.size() == 0){
				empty_layout.setVisibility(View.VISIBLE);
			}
		}
	}
	
	private void RequestData(){
		if(isFinishloadData){
			mGridView.setEnabled(false);
			isFinishloadData = false;
			if(pageIndex == 0){
				mProgressbar.setVisibility(View.VISIBLE);
			}
			empty_layout.setVisibility(View.GONE);
			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("cityId", HomeFragment.cityId);
			params.put("type", goodType);
			params.put("pageIndex", pageIndex);
			params.put("pageCount", Settings.pageCount);
			RoboHttpClient.post(HttpParameter.goodUrl,"getGoodsListByType", params, new TextHttpResponseHandler(){
				
				@Override
				public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
					ToastUtil.diaplayMesLong(getActivity(), getActivity().getResources().getString(R.string.connet_fail));
				}
				
				@Override
				public void onSuccess(int arg0, Header[] arg1, String result) {
					GetGoodsListResponse mGetGoodsListResponse = (GetGoodsListResponse) ResultParse.parseResult(result,GetGoodsListResponse.class);
					if(ResultParse.handleResutl(getActivity(), mGetGoodsListResponse)){
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
								mListView.removeFooterView(footerView);
							}else{
								isLoadMoreData = true;
								footerView.setVisibility(View.VISIBLE);
								pageIndex++;
							}
						}else{
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
					isEmpty();
					mGridView.setEnabled(true);
				}
			});
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		LogUtil.DefalutLog("HomeFragment---onActivityResult");
		if(RequestCity == requestCode){
			city = mSharedPreferences.getString(KeyUtil.CityKey, "");
			cityId = mSharedPreferences.getString(KeyUtil.CityIdKey, "");
			city_tv.setText(city);
			onSwipeRefreshLayoutRefresh();
		}
	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.title_cover:
			toActivityForResult(CityActivity.class, null, RequestCity);
			break;
		case R.id.search_btn:
			Bundle mBundle = new Bundle();
			mBundle.putString(KeyUtil.SearchTypeKey, SearchActivity.SearchGoods);	
			toActivity(SearchActivity.class, mBundle);
			break;
		case R.id.empty_layout:
			onSwipeRefreshLayoutRefresh();
			break;
		}
	}
	
	public class MyOnPageChangeListener implements OnPageChangeListener {

        @Override
        public void onPageSelected(int position) {
        	ViewUtil.changeState(viewpager_dot_layout, (position%5));
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

        @Override
        public void onPageScrollStateChanged(int arg0) {}
    }
	
	@Override
	public void onDestroyView() {
		LogUtil.DefalutLog(HomeFragment.class.getName()+"---onDestroyView");
		super.onDestroyView();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		mBaseFragment = null;
	}

}

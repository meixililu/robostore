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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.gc.materialdesign.views.ProgressBarCircularIndeterminate;
import com.loopj.android.http.TextHttpResponseHandler;
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
	
	private View headerView;
	private MyGridView mGridView;
	private AutoScrollViewPager auto_view_pager;
	private String goodType = "0";
	public static String city;
	public static BaseFragment mBaseFragment;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mBaseFragment = this;
		mSharedPreferences = SPUtil.getSharedPreferences(getActivity());
		city = mSharedPreferences.getString(KeyUtil.CityKey, "");
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
        
        mListView.addHeaderView(headerView);
		mListView.setAdapter(mHomeListViewAdapter);
		city_cover.setOnClickListener(this);
		search_btn.setOnClickListener(this);
		if(!TextUtils.isEmpty(city)){
			city_tv.setText(city);
		}else{
			toActivityForResult(CityActivity.class, null, RequestCity);
		}
		isHashFinishInitView = true;
		loadData();
	}
	
	public void onSwipeRefreshLayoutRefresh(){
		clearList();
		RequestData();
	}
	
	public void clearList(){
		goodsList.clear();
		mHomeListViewAdapter.notifyDataSetChanged();
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

	
	protected void loadData(){
		LogUtil.DefalutLog("HomeFragment---setUserVisibleHint---loadData");
		if(isHashFinishInitView){
			isHashFinishInitView = false;
			RequestData();
		}
	}
	
	private void RequestData(){
		mProgressbar.setVisibility(View.VISIBLE);
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("type", goodType);
		RoboHttpClient.get(HttpParameter.goodUrl,"getGoodsListByType", params, new TextHttpResponseHandler(){

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
					for(int i=0; i<20; i++){
						goodsList.addAll(mGoodsList);
					}
					
					mMenuAdapter.notifyDataSetChanged();
					mHomeListViewAdapter.notifyDataSetChanged();
				}
			}
			
			@Override
			public void onFinish() {
				mSwipeRefreshLayout.setRefreshing(false);
				mProgressbar.setVisibility(View.GONE);
			}
		});
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		LogUtil.DefalutLog("HomeFragment---onActivityResult");
		if(RequestCity == requestCode){
			city = mSharedPreferences.getString(KeyUtil.CityKey, "");
			city_tv.setText(city);
		}
	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.title_cover:
			toActivityForResult(CityActivity.class, null, RequestCity);
			break;
		case R.id.search_btn:
			
			break;
		}
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

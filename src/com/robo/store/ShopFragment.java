package com.robo.store;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.apache.http.Header;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.gc.materialdesign.views.ProgressBarCircularIndeterminate;
import com.loopj.android.http.TextHttpResponseHandler;
import com.robo.store.dao.GetGoodsListResponse;
import com.robo.store.dao.GoodsBase;
import com.robo.store.dao.GoodsType;
import com.robo.store.http.HttpParameter;
import com.robo.store.http.RoboHttpClient;
import com.robo.store.util.HomeUtil;
import com.robo.store.util.LogUtil;
import com.robo.store.util.ResultParse;
import com.robo.store.util.ToastUtil;

public class ShopFragment extends BaseFragment implements OnClickListener{
	
	private ProgressBarCircularIndeterminate mProgressbar;
	public SwipeRefreshLayout mSwipeRefreshLayout;
	private FrameLayout search_cover;
	private ListView mListView;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		LogUtil.DefalutLog(ShopFragment.class.getName()+"---onCreateView");
		setLayoutId(R.layout.fragment_shop);
        return super.onCreateView(inflater, container, savedInstanceState);
	}
	
	protected void initView(){
		search_cover = (FrameLayout) getView().findViewById(R.id.search_cover);
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
		
		
		search_cover.setOnClickListener(this);
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
	}
	
	public void onSwipeRefreshLayoutRefresh(){
//		clearList();
		RequestData();
	}
	
	private void RequestData(){
		mProgressbar.setVisibility(View.VISIBLE);
		HashMap<String, String> params = new HashMap<String, String>();
//		params.put("type", goodType);
		RoboHttpClient.get(HttpParameter.goodUrl,"getGoodsListByType", params, new TextHttpResponseHandler(){

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
				ToastUtil.diaplayMesLong(getActivity(), getActivity().getResources().getString(R.string.connet_fail));
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, String result) {
				GetGoodsListResponse mGetGoodsListResponse = (GetGoodsListResponse) ResultParse.parseResult(result,GetGoodsListResponse.class);
				if(ResultParse.handleResutl(getActivity(), mGetGoodsListResponse)){
					
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
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.search_cover:
			toActivity(CityActivity.class, null);
			break;
		case R.id.search_btn:
			
			break;
		}
	}

}

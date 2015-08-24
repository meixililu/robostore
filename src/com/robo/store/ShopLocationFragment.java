package com.robo.store;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.robo.store.util.LogUtil;

public class ShopLocationFragment extends BaseFragment {

	private LayoutInflater inflater;
	private MapView mMapView;
	private BaiduMap mBaiduMap;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		LogUtil.DefalutLog(ShopLocationFragment.class.getName()+"---onCreateView");
		this.inflater = inflater;
		setLayoutId(R.layout.fragment_shop_location);
        return super.onCreateView(inflater, container, savedInstanceState);
	}
	
	protected void initView(){
		mMapView = (MapView) getView().findViewById(R.id.bmapView);
		mBaiduMap = mMapView.getMap();
		mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL); 
		LatLng point = new LatLng(ShopLocationActivity.latitude, ShopLocationActivity.longitude); 
		//构建Marker图标  
		BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_poi);  
		//构建MarkerOption，用于在地图上添加Marker  
		OverlayOptions option = new MarkerOptions()  
		    .position(point)  
		    .icon(bitmap);  
		//在地图上添加Marker，并显示  
		mBaiduMap.addOverlay(option);
		MapStatus mMapStatus = new MapStatus.Builder()
		.target(point)
		.zoom(18)
		.build();
		MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
		mBaiduMap.animateMapStatus(mapStatusUpdate);
	}
	
	
	
	
	
	
	
	
	
	
	@Override
	public void onDestroyView() {
		LogUtil.DefalutLog(ShopLocationFragment.class.getName()+"---onDestroyView");
		super.onDestroyView();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	}
}

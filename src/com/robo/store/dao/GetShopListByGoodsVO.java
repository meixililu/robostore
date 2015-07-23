package com.robo.store.dao;

import java.util.List;

public class GetShopListByGoodsVO {
	private String shopName;
	private String shopId;
	private String distance;
	private int type;
	private List<MachineVO> list;
	private double longitude;
	private double latitude;
	private String shopMemo;
	
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public String getShopMemo() {
		return shopMemo;
	}
	public void setShopMemo(String shopMemo) {
		this.shopMemo = shopMemo;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public List<MachineVO> getList() {
		return list;
	}
	public void setList(List<MachineVO> list) {
		this.list = list;
	}
	public String getShopName() {
		return shopName;
	}
	public void setShopName(String shopName) {
		this.shopName = shopName;
	}
	public String getShopId() {
		return shopId;
	}
	public void setShopId(String shopId) {
		this.shopId = shopId;
	}
	public String getDistance() {
		return distance;
	}
	public void setDistance(String distance) {
		this.distance = distance;
	}
	
}

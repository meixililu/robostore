package com.robo.store.dao;

import java.util.List;

public class GetShopInfoResponse extends CommonResponse{
	private List<GoodsType> typeList;
	private List<GoodsBase> goodsList;
	private String shopName;
	private String longitude;
	private String latitude;
	private String shopMemo;
	public String getShopMemo() {
		return shopMemo;
	}
	public void setShopMemo(String shopMemo) {
		this.shopMemo = shopMemo;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public List<GoodsType> getTypeList() {
		return typeList;
	}
	public void setTypeList(List<GoodsType> typeList) {
		this.typeList = typeList;
	}
	public List<GoodsBase> getGoodsList() {
		return goodsList;
	}
	public void setGoodsList(List<GoodsBase> goodsList) {
		this.goodsList = goodsList;
	}
	public String getShopName() {
		return shopName;
	}
	public void setShopName(String shopName) {
		this.shopName = shopName;
	}
}

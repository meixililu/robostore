package com.robo.store.dao;

public class ShopBase {
	
	public static final int ITEM = 0;
	public static final int SECTION = 1;
	
	private String shopId;
	private String distance;
	private String shopName;
	private int type;//1 is secion
	
	public ShopBase(){}
	
	public ShopBase(String shopName, String distance){
		this.shopName = shopName;
		this.distance = distance;
	}
	
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
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
	public String getShopName() {
		return shopName;
	}
	public void setShopName(String shopName) {
		this.shopName = shopName;
	}
	
}

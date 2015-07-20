package com.robo.store.dao;

import java.util.List;

public class GetShopListByGoodsVO {
	private String shopName;
	private String shopId;
	private String distance;
	private List<MachineVO> list;
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

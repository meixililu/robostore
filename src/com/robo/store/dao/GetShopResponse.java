package com.robo.store.dao;

import java.util.List;

public class GetShopResponse  extends CommonResponse{
	private String shopName;
	private List<GoodsType> typeList;
	private List<GoodsBase> goodsList;
	public String getShopName() {
		return shopName;
	}
	public void setShopName(String shopName) {
		this.shopName = shopName;
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
}

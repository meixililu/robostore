package com.robo.store.dao;

import java.util.List;

public class GetGoodsListResponse extends CommonResponse{
	private List<GoodsType> typeList;
	private List<GoodsBase> goodsList;
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

package com.robo.store.dao;

import java.util.List;

public class GetShopListByGoodsList {
	private String areaLevelName;
	private List<GetShopListByGoodsVO> list;
	public String getAreaLevelName() {
		return areaLevelName;
	}
	public void setAreaLevelName(String areaLevelName) {
		this.areaLevelName = areaLevelName;
	}
	public List<GetShopListByGoodsVO> getList() {
		return list;
	}
	public void setList(List<GetShopListByGoodsVO> list) {
		this.list = list;
	}
}

package com.robo.store.dao;

import java.util.List;

public class GetShopListByGoodsResponse extends CommonResponse{
	private List<GetShopListByGoodsList> list;

	public List<GetShopListByGoodsList> getList() {
		return list;
	}

	public void setList(List<GetShopListByGoodsList> list) {
		this.list = list;
	}
}

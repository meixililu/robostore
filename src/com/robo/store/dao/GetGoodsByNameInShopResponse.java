package com.robo.store.dao;

import java.util.List;

public class GetGoodsByNameInShopResponse extends CommonResponse{
	private List<GoodsBase> goodsList; ;

	public List<GoodsBase> getGoodsList() {
		return goodsList;
	}

	public void setGoodsList(List<GoodsBase> goodsList) {
		this.goodsList = goodsList;
	}

}

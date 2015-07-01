package com.robo.store.dao;

import java.util.List;

public class GetOrdersListResponse extends CommonResponse {
	private List<OrderGoods> list;

	public List<OrderGoods> getList() {
		return list;
	}

	public void setList(List<OrderGoods> list) {
		this.list = list;
	}
}

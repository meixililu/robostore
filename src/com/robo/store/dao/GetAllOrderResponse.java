package com.robo.store.dao;

import java.util.List;

public class GetAllOrderResponse extends CommonResponse {
	private List<GetOrdersListResponse> orderList ;

	public List<GetOrdersListResponse> getOrderList() {
		return orderList;
	}

	public void setOrderList(List<GetOrdersListResponse> orderList) {
		this.orderList = orderList;
	}
}

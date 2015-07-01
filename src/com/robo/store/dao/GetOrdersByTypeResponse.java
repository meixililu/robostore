package com.robo.store.dao;

import java.util.List;

public class GetOrdersByTypeResponse extends CommonResponse{
	private List<String> orderId;
	private List<OrderDetail> list;
	
	public List<String> getOrderId() {
		return orderId;
	}
	public void setOrderId(List<String> orderId) {
		this.orderId = orderId;
	}
	public List<OrderDetail> getList() {
		return list;
	}
	public void setList(List<OrderDetail> list) {
		this.list = list;
	}
}

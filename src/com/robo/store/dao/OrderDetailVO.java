package com.robo.store.dao;


public class OrderDetailVO {
	private String orderDetailId;
	private OrderGoods orderGoods;
	public String getOrderDetailId() {
		return orderDetailId;
	}
	public void setOrderDetailId(String orderDetailId) {
		this.orderDetailId = orderDetailId;
	}
	public OrderGoods getOrderGoods() {
		return orderGoods;
	}
	public void setOrderGoods(OrderGoods orderGoods) {
		this.orderGoods = orderGoods;
	}

}

package com.robo.store.dao;

import java.util.List;


public class GetOrdersListResponse {

	private String orderId;
	private int orderStatus;
	private String orderAmount;
	private String barcode;
	private String orderTime;
	public String getOrderTime() {
		return orderTime;
	}
	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}
	private List<OrderDetailVO> detailList ;
	
	
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public int getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(int orderStatus) {
		this.orderStatus = orderStatus;
	}
	public List<OrderDetailVO> getDetailList() {
		return detailList;
	}
	public void setDetailList(List<OrderDetailVO> detailList) {
		this.detailList = detailList;
	}
	public String getOrderAmount() {
		return orderAmount;
	}
	public void setOrderAmount(String orderAmount) {
		this.orderAmount = orderAmount;
	}
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
}

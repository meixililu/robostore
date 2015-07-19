package com.robo.store.dao;

import java.util.List;

public class GetSingleOrderResponse extends CommonResponse {
	private String mallOrderCode;
	private int orderType;
	private int payType;
	private double totalPrice;
	private String payTime;
	private String orderTime;
	private List<MallOrderDetailVO> detailList;
	
	public int getPayType() {
		return payType;
	}
	public void setPayType(int payType) {
		this.payType = payType;
	}
	public double getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}
	public String getPayTime() {
		return payTime;
	}
	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}
	public String getOrderTime() {
		return orderTime;
	}
	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}
	
	public String getMallOrderCode() {
		return mallOrderCode;
	}
	public void setMallOrderCode(String mallOrderCode) {
		this.mallOrderCode = mallOrderCode;
	}
	public int getOrderType() {
		return orderType;
	}
	public void setOrderType(int orderType) {
		this.orderType = orderType;
	}
	public List<MallOrderDetailVO> getDetailList() {
		return detailList;
	}
	public void setDetailList(List<MallOrderDetailVO> detailList) {
		this.detailList = detailList;
	} 
}

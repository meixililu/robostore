package com.robo.store.dao;

import java.util.List;

public class AddOrderRequest {
	private String cityId;
	private double totalPrice;
	private List<AddOrderDetailVO> list;
	public List<AddOrderDetailVO> getList() {
		return list;
	}
	public void setList(List<AddOrderDetailVO> list) {
		this.list = list;
	}
	public String getCityId() {
		return cityId;
	}
	public void setCityId(String cityId) {
		this.cityId = cityId;
	}
	public double getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}
}

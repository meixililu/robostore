package com.robo.store.dao;

import java.util.List;

public class GetSingleOrderResponse extends CommonResponse {
	private String mallOrderCode;
	private int orderType;
	private List<MallOrderDetailVO> detailList;
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

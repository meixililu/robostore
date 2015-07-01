package com.robo.store.dao;

public class GetAllOrdersByStatusRequest extends PageCommon{
	private String mallUserId;
	private int queryType;
	
	public String getMallUserId() {
		return mallUserId;
	}
	public void setMallUserId(String mallUserId) {
		this.mallUserId = mallUserId;
	}
	public int getQueryType() {
		return queryType;
	}
	public void setQueryType(int queryType) {
		this.queryType = queryType;
	}
	
}

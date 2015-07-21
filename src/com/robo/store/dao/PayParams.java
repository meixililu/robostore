package com.robo.store.dao;

public class PayParams extends CommonResponse{
	private String appid;
	private String partnerid;
	private String prepayid;
	
	public PayParams() {
		super();
	}
	
	public PayParams(String appid, String partnerid, String prepayid) {
		super();
		this.appid = appid;
		this.partnerid = partnerid;
		this.prepayid = prepayid;
	}
	public String getAppid() {
		return appid;
	}
	public void setAppid(String appid) {
		this.appid = appid;
	}
	public String getPartnerid() {
		return partnerid;
	}
	public void setPartnerid(String partnerid) {
		this.partnerid = partnerid;
	}
	public String getPrepayid() {
		return prepayid;
	}
	public void setPrepayid(String prepayid) {
		this.prepayid = prepayid;
	}
}

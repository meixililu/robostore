package com.robo.store.dao;

import java.util.List;

public class GetSingleGoodsResponse extends CommonResponse{
	private String goodsName;
	private String retailPrice;
	private String vipPrice;
	private String vipStartDate;
	private String vipEndDate;
	private String goodsMemo;
	private String goodsSpec;
	private List<GoodsPic> picList;
	
	
	public List<GoodsPic> getPicList() {
		return picList;
	}
	public void setPicList(List<GoodsPic> picList) {
		this.picList = picList;
	}
	public String getGoodsMemo() {
		return goodsMemo;
	}
	public void setGoodsMemo(String goodsMemo) {
		this.goodsMemo = goodsMemo;
	}
	public String getGoodsSpec() {
		return goodsSpec;
	}
	public void setGoodsSpec(String goodsSpec) {
		this.goodsSpec = goodsSpec;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public String getRetailPrice() {
		return retailPrice;
	}
	public void setRetailPrice(String retailPrice) {
		this.retailPrice = retailPrice;
	}
	public String getVipPrice() {
		return vipPrice;
	}
	public void setVipPrice(String vipPrice) {
		this.vipPrice = vipPrice;
	}
	public String getVipStartDate() {
		return vipStartDate;
	}
	public void setVipStartDate(String vipStartDate) {
		this.vipStartDate = vipStartDate;
	}
	public String getVipEndDate() {
		return vipEndDate;
	}
	public void setVipEndDate(String vipEndDate) {
		this.vipEndDate = vipEndDate;
	}
}

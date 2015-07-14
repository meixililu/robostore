package com.robo.store.dao;

import java.io.Serializable;

public class GoodsBase implements Serializable {  
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String goodsBarcode;
	private String goodsName;
	private String retailPrice;
	private String vipPrice;
	private String goodsPic;
	private int number;
	private boolean isSelected;
	
	public String addNumber(){
		number++;
		return String.valueOf(number);
	}
	
	public String minusNumber(){
		if(number > 1){
			number--;
		}
		return String.valueOf(number);
	}
	
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public boolean isSelected() {
		return isSelected;
	}
	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}
	public String getGoodsPic() {
		return goodsPic;
	}
	public void setGoodsPic(String goodsPic) {
		this.goodsPic = goodsPic;
	}
	public String getGoodsBarcode() {
		return goodsBarcode;
	}
	public void setGoodsBarcode(String goodsBarcode) {
		this.goodsBarcode = goodsBarcode;
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
	
}

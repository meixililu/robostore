package com.robo.store.dao;

public class OrderGoods {

	private String goodsPic;
	private String goodsCount;
	private String goodsPrise;
	private String mallOrderId;
	private int orderStatus;
	private String goodsName;
	private String barcodeData;
	private String goodsBarcode;
	public String getGoodsBarcode() {
		return goodsBarcode;
	}
	public void setGoodsBarcode(String goodsBarcode) {
		this.goodsBarcode = goodsBarcode;
	}
	public int getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(int orderStatus) {
		this.orderStatus = orderStatus;
	}
	public String getBarcodeData() {
		return barcodeData;
	}
	public void setBarcodeData(String barcodeData) {
		this.barcodeData = barcodeData;
	}


	public String getMallOrderId() {
		return mallOrderId;
	}
	public void setMallOrderId(String mallOrderId) {
		this.mallOrderId = mallOrderId;
	}
	public String getGoodsPic() {
		return goodsPic;
	}
	public void setGoodsPic(String goodsPic) {
		this.goodsPic = goodsPic;
	}
	public String getGoodsCount() {
		return goodsCount;
	}
	public void setGoodsCount(String goodsCount) {
		this.goodsCount = goodsCount;
	}
	public String getGoodsPrise() {
		return goodsPrise;
	}
	public void setGoodsPrise(String goodsPrise) {
		this.goodsPrise = goodsPrise;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	
}

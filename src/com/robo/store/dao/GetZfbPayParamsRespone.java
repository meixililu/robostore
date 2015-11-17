package com.robo.store.dao;

public class GetZfbPayParamsRespone extends CommonResponse{
	
	private AlipayParams alipayParams;

	public AlipayParams getAlipayParams() {
		return alipayParams;
	}

	public void setAlipayParams(AlipayParams alipayParams) {
		this.alipayParams = alipayParams;
	}
	
}

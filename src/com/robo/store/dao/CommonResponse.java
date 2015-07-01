package com.robo.store.dao;

public class CommonResponse {
	private String status;
	private String errorMsg;

	public CommonResponse() {
		super();
	}

	public CommonResponse(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

}

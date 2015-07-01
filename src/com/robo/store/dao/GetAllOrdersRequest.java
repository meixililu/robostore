package com.robo.store.dao;

public class GetAllOrdersRequest extends CommonRequest{
	private String mallUserId;
	private int pageCount;
	private int pageIndex;

	public int getPageCount() {
		return pageCount;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

	public int getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}

	public String getMallUserId() {
		return mallUserId;
	}

	public void setMallUserId(String mallUserId) {
		this.mallUserId = mallUserId;
	}
}

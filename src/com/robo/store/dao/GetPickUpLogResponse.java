package com.robo.store.dao;

import java.util.List;

public class GetPickUpLogResponse extends CommonResponse{
	private List<PickUpLog> detailList;

	public List<PickUpLog> getDetailList() {
		return detailList;
	}

	public void setDetailList(List<PickUpLog> detailList) {
		this.detailList = detailList;
	}
	
	
}

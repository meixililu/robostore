package com.robo.store.dao;

import java.util.List;

public class GetPickUpLogResponse extends CommonResponse{
	private List<GetPickUpLogVO> list;

	public List<GetPickUpLogVO> getList() {
		return list;
	}

	public void setList(List<GetPickUpLogVO> list) {
		this.list = list;
	}
}

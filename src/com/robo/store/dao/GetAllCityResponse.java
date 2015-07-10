package com.robo.store.dao;

import java.util.List;

public class GetAllCityResponse extends CommonResponse{
	private List<CityVO> list;

	public List<CityVO> getList() {
		return list;
	}

	public void setList(List<CityVO> list) {
		this.list = list;
	}
	
}

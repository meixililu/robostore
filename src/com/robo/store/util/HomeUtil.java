package com.robo.store.util;

import java.util.List;

import com.robo.store.dao.GoodsType;

public class HomeUtil {

	public static void setSelectedMenu(List<GoodsType> typeList, String currentType){
		for(GoodsType mGoodsType : typeList){
			if(mGoodsType.getGoodsTypeId().equals(currentType)){
				mGoodsType.setIsSelect(1);
			}else{
				mGoodsType.setIsSelect(0);
			}
		}
	}
	
}

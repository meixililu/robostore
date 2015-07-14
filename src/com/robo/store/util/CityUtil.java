package com.robo.store.util;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

import com.liucanwen.citylist.model.CityItem;
import com.liucanwen.citylist.widget.ContactItemInterface;
import com.liucanwen.citylist.widget.pinyin.PinYin;
import com.robo.store.dao.CityVO;

public class CityUtil {

	public static List<ContactItemInterface> getSampleContactList(List<CityVO> mCityVOList) {
		List<ContactItemInterface> list = new ArrayList<ContactItemInterface>();
		try {
			for (CityVO mCityVO : mCityVOList) {
				String cityName = mCityVO.getCityName();
				CityItem item = new CityItem(mCityVO.getCityId(), cityName, PinYin.getPinYin(cityName));
				list.add(item);
				if(cityName.contains("北京")){
					list.add(new CityItem(mCityVO.getCityId(), cityName, "2"));
				}else if(cityName.contains("上海")){
					list.add(new CityItem(mCityVO.getCityId(), cityName, "2"));
				}else if(cityName.contains("深圳")){
					list.add(new CityItem(mCityVO.getCityId(), cityName, "2"));
				}else if(cityName.contains("广州")){
					list.add(new CityItem(mCityVO.getCityId(), cityName, "2"));
				}else if(cityName.contains("南京")){
					list.add(new CityItem(mCityVO.getCityId(), cityName, "2"));
				}else if(cityName.contains("郑州")){
					list.add(new CityItem(mCityVO.getCityId(), cityName, "2"));
				}
			}
			list.add(new CityItem("定位中", "1"));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}
	
	public static void setLocationCity(String name, List<ContactItemInterface> contactList){
		LogUtil.DefalutLog("setLocationCity---name:"+name);
		CityItem item = (CityItem) contactList.get(0);
		for (int i=0; i<contactList.size(); i++) {
			CityItem mCityItem = (CityItem) contactList.get(i);
			if( !TextUtils.isEmpty(mCityItem.getCityId()) && !TextUtils.isEmpty(mCityItem.getNickName()) ){
				if(name.contains(mCityItem.getNickName())){
					LogUtil.DefalutLog("setLocationCity---name---succee");
					item.setCityId(mCityItem.getCityId());
					item.setNickName(mCityItem.getNickName());
				}
			}
		}
	}

}

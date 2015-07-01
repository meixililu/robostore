package com.robo.store.util;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONObject;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.robo.store.dao.CommonResponse;

public class ResultParse {
	
	public static final String ResultOk = "0";

	public static CommonResponse parseResult(String result,Class mClass){
		CommonResponse ResultDao = null;
		try {
			if(!TextUtils.isEmpty(result)){
				JSONObject jObject = new JSONObject(result);
				if(jObject.has("dataList")){
					String dataList = jObject.getString("dataList");
					LogUtil.DefalutLog("dataList---:"+dataList);
					ObjectMapper objectMapper = new ObjectMapper();
					ResultDao = objectMapper.readValue(dataList,  mClass);
//					ResultDao = new Gson().fromJson(dataList,  mClass);
					return ResultDao;
				}else{
					return ResultDao;
				}
			}else{
				return ResultDao;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResultDao;
		}
	}
	
	public static boolean handleResutl(Context mContext, CommonResponse mResultDao){
		boolean data = false;
		if(mResultDao != null){
			if(mResultDao.getStatus().equals(ResultOk)){
				data = true;
			}else{
				ToastUtil.diaplayMesLong(mContext, mResultDao.getErrorMsg());
			}
		}
		return data;
	}
	
}

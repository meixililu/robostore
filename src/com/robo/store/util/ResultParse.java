package com.robo.store.util;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.robo.store.dao.CommonResponse;

public class ResultParse {
	
	public static final String ResultOk = "0";
	
	public static final String TokenOut = "10003";

	public static CommonResponse parseResult(String result,Class mClass){
		CommonResponse ResultDao = null;
		try {
			LogUtil.DefalutLog("---result---:"+result);
			if(!TextUtils.isEmpty(result)){
				JSONObject jObject = new JSONObject(result);
				if(jObject.has("dataList")){
					String dataList = jObject.getString("dataList");
					ObjectMapper objectMapper = new ObjectMapper();
					ResultDao = objectMapper.readValue(dataList,  mClass);
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
			}else if(mResultDao.getStatus().equals(TokenOut)){
				SharedPreferences mSharedPreferences = SPUtil.getSharedPreferences(mContext);
				LoginUtil.login(mContext, mSharedPreferences);
				ToastUtil.diaplayMesLong(mContext, "用户信息更新成功，请刷新重试！");
			}else{
				ToastUtil.diaplayMesLong(mContext, mResultDao.getErrorMsg());
			}
		}
		return data;
	}
	
}

package com.robo.store.util;

import android.content.Context;
import android.text.TextUtils;
import android.widget.EditText;

public class ValidUtil {

	public static boolean validPhoneData(Context mContext, String userName){
		boolean isvalid = true;
		if(TextUtils.isEmpty(userName)){
			ToastUtil.diaplayMesShort(mContext, "请输入手机号");
			isvalid = false;
		}else if(userName.length() != 11){
			ToastUtil.diaplayMesShort(mContext, "请输入正确的手机号");
			isvalid = false;
		}
		return isvalid;
	}
	
	public static boolean validData(Context mContext, String data, String toast){
		boolean isvalid = true;
		if(TextUtils.isEmpty(data)){
			ToastUtil.diaplayMesShort(mContext, toast);
			isvalid = false;
		}
		return isvalid;
	}
}

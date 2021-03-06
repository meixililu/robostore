package com.robo.store.http;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

public class HttpParameter {
	
	public static final String rootUrl = "http://www.robochina.com.cn";
//	public static final String rootUrl = "http://123.57.8.253:8080";

	public static final String baseUrl = rootUrl +"/robo/base";
	
	public static final String goodUrl = rootUrl +"/robo/goods";
	
	public static final String orderUrl = rootUrl +"/robo/order";
	
	public static final String shopsUrl = rootUrl +"/robo/sys";
	
	public static final String payUrl = rootUrl +"/robopay/weixin/pay";
	
	public static final String zfbUrl = rootUrl +"/robopay/alipay/pay";
	//RequestHeader
	public static String softVer;
	
	public static int softVerCode;
	
	public static String channelId;
	
	public static String platform = "ANDROID";
	
	public static String systemVer = android.os.Build.VERSION.RELEASE;
	
	public static String machId;
	
	public static String accessToken;
	
	public static void initRequestHeader(Context mContext){
		getVersion(mContext);
		getDeviceId(mContext);
		channelId = getChanel(mContext);
	}
	
	public static void getVersion(Context mContext) {
		try {
			PackageManager manager = mContext.getPackageManager();
			PackageInfo info = manager.getPackageInfo(mContext.getPackageName(), 0);
			softVer = info.versionName;
			softVerCode = info.versionCode;
		} catch (Exception e) {
			softVer = "1.0";
			e.printStackTrace();
		}
	}
	
	public static void getDeviceId(Context mContext){
		TelephonyManager tm = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);  
		machId = tm.getDeviceId();  
		if(TextUtils.isEmpty(machId)){ 
			WifiManager wm = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE); 
			machId = wm.getConnectionInfo().getMacAddress(); 
		}
	}
	
	public static String getChanel(Context mContext) {
		String CHANNELID = "100001";
		try {
			ApplicationInfo ai = mContext.getPackageManager().getApplicationInfo(mContext.getPackageName(), PackageManager.GET_META_DATA);
			Object value = ai.metaData.get("ROBOCHANNEL");
			if (value != null) {
				CHANNELID = value.toString();
			}
			if(TextUtils.isEmpty(CHANNELID)){
				CHANNELID = "100001";
			}
		} catch (Exception e) {
			CHANNELID = "100001";
		}
		return CHANNELID;
	}
	
}

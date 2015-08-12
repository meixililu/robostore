package com.robo.store.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class HttpUtil {

	public static boolean checkNetwork(final Context mContext){
		ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);    
        NetworkInfo networkinfo = connectivityManager.getActiveNetworkInfo();    
        if (networkinfo == null || !networkinfo.isAvailable()) {  // 当前网络不可用  
        	Toast.makeText(mContext, "没有可用的网络连接,请打开网络连接！", 0).show();
        	return false;
        }else{
        	return true;
        }
	}
	
	public static boolean isNetworkAvailable(Context mContext){
		ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);    
        NetworkInfo networkinfo = connectivityManager.getActiveNetworkInfo();    
        if (networkinfo != null && networkinfo.isAvailable()) {  
        	return true;
        }else{
        	// 当前网络不可用  
        	return false;
        }
	}
}

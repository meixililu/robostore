package com.robo.store.util;

import android.util.Log;

public class LogUtil {
	
	public static boolean DEBUG = true;
	/**默认log，TAG�??SafeLottery
	 * @param logContent
	 */
	public static void DefalutLog(String logContent){
		if(DEBUG) Log.d("RoboStore", logContent);
	}
	
	/**
	 * @param logContent
	 */
	public static void SystemLog(String logContent){
		if(DEBUG) System.out.println(logContent);
	}
	
	/**
	 * @param TAG
	 * @param logContent
	 */
	public static void CustomLog(String TAG, String logContent){
		if(DEBUG) Log.d(TAG, logContent);
	}

	/**
	 * @param logContent
	 */
	public static void ErrorLog(String logContent){
		if(DEBUG) Log.d("RoboStore","Error---"+logContent);
	}
	
	/**
	 * @param logContent
	 */
	public static void ExceptionLog(String logContent){
		if(DEBUG) Log.e("RoboStore","Exception---"+logContent);
	}
	
	/**
	 * @param androidpn
	 */
	public static void AndroidPnLog(String logContent){
		if(DEBUG) Log.e("RoboStore","Exception---"+logContent);
	}
	
}

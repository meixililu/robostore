package com.robo.store.util;

import java.util.HashMap;

import org.apache.http.Header;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;

import com.gc.materialdesign.widgets.Dialog;
import com.robo.store.dao.AppVersionResponse;
import com.robo.store.http.HttpParameter;
import com.robo.store.http.RoboHttpClient;
import com.robo.store.http.TextHttpResponseHandler;

public class APKDownloadUtil {

	public static void CheckUpdate(Context mContext){
		RequestData(mContext);
	}
	
	private static void RequestData(final Context mContext){
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("appName", "robo");//roboisp
		params.put("appVersion", String.valueOf(HttpParameter.softVerCode));
		params.put("appType", "1");
		RoboHttpClient.post(HttpParameter.shopsUrl, "appVersion", params, new TextHttpResponseHandler(){
			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
			}
			@Override
			public void onSuccess(int arg0, Header[] arg1, String result) {
				AppVersionResponse mResponse = (AppVersionResponse) ResultParse.parseResult(result,AppVersionResponse.class);
				if(ResultParse.handleResutl(mContext, mResponse)){
//					mResponse.setUpdateType(1);
//					mResponse.setDownloadUrl("http://robouserapp.oss-cn-beijing.aliyuncs.com/RoboStore.apk");
//					mResponse.setMessage("1、每日一句可以播放音频；\n 2、新增口语练习与评测； \n 3、优化程序；");
					if(mResponse.getUpdateType() == 1){
						updateDialog(mContext, false, mResponse);
					}else if(mResponse.getUpdateType() == 2){
						updateDialog(mContext, true, mResponse);
					}
				}
			}
			@Override
			public void onFinish() {
			}
		});
	}
	
	private static void updateDialog(final Context mContext, boolean isForce, final AppVersionResponse mResponse){
		Dialog dialog = new Dialog(mContext, "发现新版本", mResponse.getMessage());
		dialog.addAcceptButton("升级");
		if(!isForce){
			dialog.addCancelButton("稍后");
		}
		dialog.setOnAcceptButtonClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				DownloadFileUtil.DownloadFile(mContext,mResponse.getDownloadUrl());
			}
		});
		dialog.setCancelable(false);
		dialog.show();
	}
}

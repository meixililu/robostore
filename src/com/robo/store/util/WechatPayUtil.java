package com.robo.store.util;

import java.security.MessageDigest;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.tencent.mm.sdk.modelpay.PayReq;

public class WechatPayUtil {
	
	public static final String APP_KEY = "";
	
	public static String getSignData(PayReq request){
		List<NameValuePair> signParams = new LinkedList<NameValuePair>();
		signParams.add(new BasicNameValuePair("appid", request.appId));
		signParams.add(new BasicNameValuePair("appkey", APP_KEY));
		signParams.add(new BasicNameValuePair("noncestr", request.nonceStr));
		signParams.add(new BasicNameValuePair("package", request.packageValue));
		signParams.add(new BasicNameValuePair("partnerid", request.partnerId));
		signParams.add(new BasicNameValuePair("prepayid", request.prepayId));
		signParams.add(new BasicNameValuePair("timestamp", request.timeStamp));
		String stringA = genSign(signParams);
		String stringSignTemp = stringA + "&key=27ba29d586d6416b8b4266d3daeab797";
		String sign = Md5.d5(stringSignTemp).toUpperCase();
		LogUtil.DefalutLog("wechat---sign:" + sign);
		return sign;
	}

	public static String genNonceStr() {
		Random random = new Random();
		return Md5.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
	}
	
	public static long genTimeStamp() {
		return System.currentTimeMillis() / 1000;
	}
	
	public static String genSign(List<NameValuePair> params) {
		StringBuilder sb = new StringBuilder();
		int i = 0;
		for (; i < params.size() - 1; i++) {
			sb.append(params.get(i).getName());
			sb.append('=');
			sb.append(params.get(i).getValue());
			sb.append('&');
		}
		sb.append(params.get(i).getName());
		sb.append('=');
		sb.append(params.get(i).getValue());
		
		String sha1 = sha1(sb.toString());
		return sha1;
	}
	
	public static String sha1(String str) {
		if (str == null || str.length() == 0) {
			return null;
		}
		
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
		
		try {
			MessageDigest mdTemp = MessageDigest.getInstance("SHA1");
			mdTemp.update(str.getBytes());
			
			byte[] md = mdTemp.digest();
			int j = md.length;
			char buf[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				buf[k++] = hexDigits[byte0 >>> 4 & 0xf];
				buf[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(buf);
		} catch (Exception e) {
			return null;
		}
	}
	
}

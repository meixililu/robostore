package com.robo.store.util;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;

import com.alipay.sdk.app.PayTask;
import com.robo.store.dao.AlipayParams;

public class ZFBpayUtil {
	
	public static final int SDK_PAY_FLAG = 1;
	public static final int SDK_CHECK_FLAG = 2;

	public static void startZFBpay(final Activity mActivity, final Handler mHandler,AlipayParams alipayParams){
//		check(mActivity,mHandler);
		String orderInfo = getOrderInfo(alipayParams);
		final String payInfo = orderInfo + "&sign=\"" + alipayParams.getSign() + "\"&" + getSignType();
		LogUtil.DefalutLog("---payInfo---"+payInfo);
		Runnable payRunnable = new Runnable() {
			@Override
			public void run() {
				PayTask alipay = new PayTask(mActivity);
				String result = alipay.pay(payInfo);
				Message msg = new Message();
				msg.what = SDK_PAY_FLAG;
				msg.obj = result;
				mHandler.sendMessage(msg);
			}
		};
		Thread payThread = new Thread(payRunnable);
		payThread.start();
	}
	
	public static String getOrderInfo(AlipayParams alipayParams) {

		// 签约合作者身份ID
		String orderInfo = "partner=" + "\"" + alipayParams.getPartner() + "\"";

		// 签约卖家支付宝账号
		orderInfo += "&seller_id=" + "\"" + alipayParams.getSeller_id() + "\"";

		// 商户网站唯一订单号
		orderInfo += "&out_trade_no=" + "\"" + alipayParams.getOut_trade_no() + "\"";

		// 商品名称
		orderInfo += "&subject=" + "\"" + alipayParams.getSubject() + "\"";

		// 商品详情
		orderInfo += "&body=" + "\"" + alipayParams.getBody() + "\"";

		// 商品金额
		orderInfo += "&total_fee=" + "\"" + alipayParams.getTotal_fee() + "\"";

		// 服务器异步通知页面路径
		orderInfo += "&notify_url=" + "\"" + alipayParams.getNotify_url()	+ "\"";

		// 服务接口名称， 固定值
		orderInfo += "&service=\"mobile.securitypay.pay\"";

		// 支付类型， 固定值
		orderInfo += "&payment_type=\"1\"";

		// 参数编码， 固定值
		orderInfo += "&_input_charset=\"utf-8\"";

		// 设置未付款交易的超时时间
				// 默认30分钟，一旦超时，该笔交易就会自动被关闭。
				// 取值范围：1m～15d。
				// m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
				// 该参数数值不接受小数点，如1.5h，可转换为90m。
//		orderInfo += "&it_b_pay=\"30m\"";

		// extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
				// orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

				// 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
		//orderInfo += "&return_url=\"m.alipay.com\"";

		// 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
				// orderInfo += "&paymethod=\"expressGateway\"";

		return orderInfo;
	}
	
	public static void check(final Activity mActivity, final Handler mHandler) {
		Runnable checkRunnable = new Runnable() {

			@Override
			public void run() {
				PayTask payTask = new PayTask(mActivity);
				boolean isExist = payTask.checkAccountIfExist();
				Message msg = new Message();
				msg.what = SDK_CHECK_FLAG;
				msg.obj = isExist;
				mHandler.sendMessage(msg);
			}
		};
		Thread checkThread = new Thread(checkRunnable);
		checkThread.start();

	}
	
	public static String getSignType() {
		return "sign_type=\"RSA\"";
	}

	
}

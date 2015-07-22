package com.robo.store.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.robo.store.BaseActivity;
import com.robo.store.R;
import com.robo.store.util.LogUtil;
import com.robo.store.util.ToastUtil;
import com.robo.store.util.WechatPayUtil;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelpay.PayResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
/**
 * 微信支付结果页面
 *
 * @version 1.0.0
 */
public class WXPayEntryActivity extends BaseActivity implements IWXAPIEventHandler {

	//b97e5f7e07bfd0ccd8c539c138f2be97   zhengshi
	private TextView result;
	private IWXAPI api;
	
	public static boolean isPaySuccee;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wechat_payresult);
		api = WXAPIFactory.createWXAPI(this, WechatPayUtil.APP_ID);
		api.handleIntent(getIntent(), this);
		
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
	}

	@Override
	public void onResp(BaseResp resp) {
		LogUtil.ErrorLog("=======weixin onPayFinish onResp==" + resp.errCode);
		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
			// 微信支付
			showPayResult(resp);
		}
	}

	private void showPayResult(BaseResp resp) {
		result = (TextView)findViewById(R.id.wechat_result);
		String msg = "";
		isPaySuccee = false;
		switch (resp.errCode) {
		case BaseResp.ErrCode.ERR_OK:
			PayResp res = (PayResp) resp;
			String func = res.extData;
			msg = "支付成功！";
			isPaySuccee = true;
			break;
		case BaseResp.ErrCode.ERR_AUTH_DENIED:
			msg = "认证失败！" +"("+ resp.errCode +")";
			break;
		case BaseResp.ErrCode.ERR_SENT_FAILED:
			msg = "支付失败！"+"("+ resp.errCode +")";
			break;
		case BaseResp.ErrCode.ERR_USER_CANCEL:
			msg = "用户取消！"+"("+ resp.errCode +")";
			break;
		case BaseResp.ErrCode.ERR_UNSUPPORT:
			msg = "不支持操作！"+"("+ resp.errCode +")";
			break;
		case BaseResp.ErrCode.ERR_COMM:
			msg = "支付失败！"+"("+ resp.errCode +")";
			break;
		}
		if(result != null){
			result.setText(msg);
		}
		ToastUtil.diaplayMesShort(this, msg);
	}

}
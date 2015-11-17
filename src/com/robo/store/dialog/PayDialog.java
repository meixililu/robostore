package com.robo.store.dialog;

import java.util.HashMap;

import org.apache.http.Header;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout.LayoutParams;

import com.gc.materialdesign.views.ButtonFlat;
import com.robo.store.R;
import com.robo.store.dao.CommonResponse;
import com.robo.store.http.HttpParameter;
import com.robo.store.http.RoboHttpClient;
import com.robo.store.http.TextHttpResponseHandler;
import com.robo.store.listener.onPayTypeSelectedListener;
import com.robo.store.util.LogUtil;
import com.robo.store.util.ResultParse;
import com.robo.store.util.ToastUtil;



public class PayDialog extends Dialog implements OnClickListener{

	private Context context;
	private ButtonFlat wechat_pay,zfb_pay; 
	private onPayTypeSelectedListener mListener;
	
	public onPayTypeSelectedListener getmListener() {
		return mListener;
	}

	public void setmListener(onPayTypeSelectedListener mListener) {
		this.mListener = mListener;
	}

	public PayDialog(Context context, String orderId,onPayTypeSelectedListener mListener) {
		super(context,R.style.dialog);
		this.context = context;
		this.mListener = mListener;
	}
	
	public PayDialog(Context context) {
		super(context,R.style.dialog);
		this.context = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pay_dialog);
		getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		wechat_pay = (ButtonFlat) findViewById(R.id.wechat_pay);
		zfb_pay = (ButtonFlat) findViewById(R.id.zfb_pay);
		
		wechat_pay.setOnClickListener(this);
		zfb_pay.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.wechat_pay:
			if(mListener != null){
				mListener.wechatPay();
			}
			PayDialog.this.dismiss();
			break;
		case R.id.zfb_pay:
			if(mListener != null){
				mListener.zfbPay();
			}
			PayDialog.this.dismiss();
			break;
		}
	}
	
}

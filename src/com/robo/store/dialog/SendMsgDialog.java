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

import com.robo.store.R;
import com.robo.store.dao.CommonResponse;
import com.robo.store.http.HttpParameter;
import com.robo.store.http.RoboHttpClient;
import com.robo.store.http.TextHttpResponseHandler;
import com.robo.store.util.LogUtil;
import com.robo.store.util.ResultParse;
import com.robo.store.util.ToastUtil;



public class SendMsgDialog extends Dialog implements OnClickListener{

	private Context context;
	private EditText user_phone_input,mesg_input; 
	private Button send_btn_cancle,send_btn_sure; 
	private String message,orderId,phone;
	
	public SendMsgDialog(Context context, String orderId, String message) {
		super(context,R.style.dialog);
		this.context = context;
		this.orderId = orderId;
		this.message = message;
	}
	
	public SendMsgDialog(Context context) {
		super(context,R.style.dialog);
		this.context = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_send_msg);
		getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		user_phone_input = (EditText) findViewById(R.id.user_phone_input);
		mesg_input = (EditText) findViewById(R.id.mesg_input);
		send_btn_cancle = (Button) findViewById(R.id.send_btn_cancle);
		send_btn_sure = (Button) findViewById(R.id.send_btn_sure);
		
		send_btn_cancle.setOnClickListener(this);
		send_btn_sure.setOnClickListener(this);
		mesg_input.setText(message);
	}
	
	private boolean validData(){
		boolean isvalid = true;
		phone = user_phone_input.getText().toString().trim();
		message = mesg_input.getText().toString().trim();
		if(TextUtils.isEmpty(phone)){
			ToastUtil.diaplayMesShort(context, "请输入手机号");
			isvalid = false;
		}
		if(TextUtils.isEmpty(message)){
			ToastUtil.diaplayMesShort(context, "请输发送内容");
			isvalid = false;
		}
		return isvalid;
	}
	
	private void RequestData(){
		if(validData()){
			final ProgressDialog progressDialog = ProgressDialog.show(context, "", "正在发送...", true, false);
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("orderId", orderId);
			params.put("message", message);
			params.put("mobile", phone);
			RoboHttpClient.post(HttpParameter.shopsUrl, "sendPickUpSms", params, new TextHttpResponseHandler(){
				
				@Override
				public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
					ToastUtil.diaplayMesLong(context, "连接失败，请重试！");
				}
				
				@Override
				public void onSuccess(int arg0, Header[] arg1, String result) {
					LogUtil.DefalutLog(result);
					CommonResponse mResponse = (CommonResponse) ResultParse.parseResult(result,CommonResponse.class);
					if(ResultParse.handleResutl(context, mResponse)){
						ToastUtil.diaplayMesLong(context, "发送成功");
						SendMsgDialog.this.dismiss();
					}
				}
				
				@Override
				public void onFinish() {
					progressDialog.dismiss();
				}
			});
		}
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.send_btn_cancle:
			SendMsgDialog.this.dismiss();
			break;
		case R.id.send_btn_sure:
			RequestData();
			break;
		}
	}
	
}

package com.robo.store;

import java.util.HashMap;

import org.apache.http.Header;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.robo.store.dao.CreateSendMessageResponse;
import com.robo.store.dialog.SendMsgDialog;
import com.robo.store.http.HttpParameter;
import com.robo.store.http.RoboHttpClient;
import com.robo.store.http.TextHttpResponseHandler;
import com.robo.store.util.DrawableUtil;
import com.robo.store.util.KeyUtil;
import com.robo.store.util.LogUtil;
import com.robo.store.util.ResultParse;
import com.robo.store.util.ToastUtil;

public class QRCodeActivity extends BaseActivity implements View.OnClickListener{

	private FrameLayout send_cover;
	private ImageView order_code_img;
	private TextView order_code_tv;
	private String qrCode,qrCodeData,orderId;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_qr_code);
		init();
	}
	
	private void init(){
		Bundle mBundle = getIntent().getBundleExtra(KeyUtil.BundleKey);
		if(mBundle != null){
			orderId = mBundle.getString(KeyUtil.OrderIdKey);
			qrCode = mBundle.getString(KeyUtil.QRCodeKey);
			qrCodeData = mBundle.getString(KeyUtil.QRCodeDataKey);
		}
		send_cover = (FrameLayout) findViewById(R.id.send_cover);
		order_code_img = (ImageView) findViewById(R.id.order_code_img);
		order_code_tv = (TextView) findViewById(R.id.order_code_tv);
		order_code_img.setImageBitmap(DrawableUtil.stringtoBitmap(qrCodeData));
		order_code_tv.setText(qrCode);
		
		send_cover.setOnClickListener(this);
	}
	
	private void RequestData(){
		final ProgressDialog progressDialog = ProgressDialog.show(this, "", "正在加载...", true, false);
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("orderId", orderId);
		RoboHttpClient.post(HttpParameter.shopsUrl, "createMessage", params, new TextHttpResponseHandler(){

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
				ToastUtil.diaplayMesLong(QRCodeActivity.this, "连接失败，请重试！");
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, String result) {
				LogUtil.DefalutLog(result);
				CreateSendMessageResponse mResponse = (CreateSendMessageResponse) ResultParse.parseResult(result,CreateSendMessageResponse.class);
				if(ResultParse.handleResutl(QRCodeActivity.this, mResponse)){
					showSendDialog(mResponse);
				}
			}
			
			@Override
			public void onFinish() {
				progressDialog.dismiss();
			}
		});
	}
	
	private void showSendDialog(CreateSendMessageResponse mResponse){
		SendMsgDialog dialog = new SendMsgDialog(this,orderId,mResponse.getMessage());
		dialog.show();
	}
	
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch(v.getId()){
		case R.id.send_cover:
			RequestData();
			break;
		}
	}
	
}

package com.robo.store;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.robo.store.util.DrawableUtil;
import com.robo.store.util.KeyUtil;

public class QRCodeActivity extends BaseActivity {

	private ImageView order_code_img;
	private TextView order_code_tv;
	private String qrCode,qrCodeData;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_qr_code);
		init();
	}
	
	private void init(){
		Bundle mBundle = getIntent().getBundleExtra(KeyUtil.BundleKey);
		if(mBundle != null){
			qrCode = mBundle.getString(KeyUtil.QRCodeKey);
			qrCodeData = mBundle.getString(KeyUtil.QRCodeDataKey);
		}
		order_code_img = (ImageView) findViewById(R.id.order_code_img);
		order_code_tv = (TextView) findViewById(R.id.order_code_tv);
		order_code_img.setImageBitmap(DrawableUtil.stringtoBitmap(qrCodeData));
		order_code_tv.setText(qrCode);
	}
	
}

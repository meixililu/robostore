package com.robo.store;

import java.util.HashMap;

import org.apache.http.Header;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.loopj.android.http.TextHttpResponseHandler;
import com.robo.store.dao.GetSingleGoodsResponse;
import com.robo.store.http.HttpParameter;
import com.robo.store.http.RoboHttpClient;
import com.robo.store.util.KeyUtil;
import com.robo.store.util.LogUtil;
import com.robo.store.util.ResultParse;
import com.robo.store.util.ToastUtil;

public class GoodsDetailActivity extends BaseActivity implements View.OnClickListener{

	private FrameLayout back_btn_cover;
	private ViewPager viewpager;
	private LinearLayout viewpager_dot_layout;
	private TextView good_price_new,good_price_old,good_newprice_end;
	private TextView good_name,good_des;
	private ImageView minus_img,plus_img;
	private TextView number_txt,goods_spec;
	private TextView good_msg;
	
	private FrameLayout add_to_cart_cover,buy_now_cover;
	private TextView get_goods_date;
	
	private String goodsBarcode;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_goods_detail);
		init();
		RequestData();
	}
	
	private void init(){
		goodsBarcode = getIntent().getStringExtra(KeyUtil.GoodsIdKey);
		back_btn_cover = (FrameLayout) findViewById(R.id.back_btn_cover);
		viewpager = (ViewPager) findViewById(R.id.viewpager);
		viewpager_dot_layout = (LinearLayout) findViewById(R.id.viewpager_dot_layout);
		good_price_new = (TextView) findViewById(R.id.good_price_new);
		good_price_old = (TextView) findViewById(R.id.good_price_old);
		good_newprice_end = (TextView) findViewById(R.id.good_newprice_end);
		
		good_name = (TextView) findViewById(R.id.good_name);
		good_des = (TextView) findViewById(R.id.good_des);
		
		minus_img = (ImageView) findViewById(R.id.minus_img);
		plus_img = (ImageView) findViewById(R.id.plus_img);
		number_txt = (TextView) findViewById(R.id.number_txt);
		goods_spec = (TextView) findViewById(R.id.goods_spec);
		
		good_msg = (TextView) findViewById(R.id.good_msg);
		add_to_cart_cover = (FrameLayout) findViewById(R.id.add_to_cart_cover);
		buy_now_cover = (FrameLayout) findViewById(R.id.buy_now_cover);
		
		back_btn_cover.setOnClickListener(this);
		minus_img.setOnClickListener(this);
		plus_img.setOnClickListener(this);
		minus_img.setOnClickListener(this);
		add_to_cart_cover.setOnClickListener(this);
		buy_now_cover.setOnClickListener(this);
	}
	
	private void setData(GetSingleGoodsResponse mGetSingleGoodsResponse){
		good_price_new.setText(mGetSingleGoodsResponse.getVipPrice());
		good_price_old.setText("￥" + mGetSingleGoodsResponse.getRetailPrice());
		good_name.setText(mGetSingleGoodsResponse.getGoodsName());
		good_msg.setText(mGetSingleGoodsResponse.getGoodsMemo());
		goods_spec.setText(mGetSingleGoodsResponse.getGoodsSpec());
	}
	
	private void RequestData(){
		showSucceeDialog();
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("goodsBarcode", goodsBarcode);
		RoboHttpClient.get(HttpParameter.goodUrl, "getSingleGoods", params, new TextHttpResponseHandler(){

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
				ToastUtil.diaplayMesLong(GoodsDetailActivity.this, "连接失败，请重试！");
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, String result) {
				LogUtil.DefalutLog(result);
				GetSingleGoodsResponse mGetSingleGoodsResponse = (GetSingleGoodsResponse) ResultParse.parseResult(result,GetSingleGoodsResponse.class);
				if(ResultParse.handleResutl(GoodsDetailActivity.this, mGetSingleGoodsResponse)){
					setData(mGetSingleGoodsResponse);
				}
			}
			
			@Override
			public void onFinish() {
				
			}
		});
	}
	
	private void showSucceeDialog(){
//		progressDialog = ProgressDialog.show(this, "", "正在登录...", true, false);
	}
	
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch(v.getId()){
		case R.id.back_btn_cover:
			GoodsDetailActivity.this.finish();
			break;
		case R.id.forget_pwd_cover:
			
			break;
		case R.id.login_btn:
			
			break;
		}
	}
	
	
}

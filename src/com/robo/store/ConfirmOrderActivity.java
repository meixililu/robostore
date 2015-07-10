package com.robo.store;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.robo.store.adapter.ConfirmToPayListViewAdapter;
import com.robo.store.dao.GoodsBase;
import com.robo.store.util.CartUtil;
import com.robo.store.util.ToastUtil;

public class ConfirmOrderActivity extends BaseActivity implements OnClickListener{

	private ListView content_lv;
	private RadioButton rb_weixin,rb_zhifubao;
	private TextView total_tv;
	private Button confirm_to_pay;
	
	private List<GoodsBase> confirmList;
	private LayoutInflater inflater;
	private ConfirmToPayListViewAdapter mAdapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(getResources().getString(R.string.confirm_order));
		setContentView(R.layout.activity_confirm_order);
		init();
	}
	
	private void init(){
		inflater = LayoutInflater.from(this);
		confirmList = new ArrayList<GoodsBase>();
		content_lv = (ListView) findViewById(R.id.content_lv);
		rb_weixin = (RadioButton) findViewById(R.id.rb_weixin);
		rb_zhifubao = (RadioButton) findViewById(R.id.rb_zhifubao);
		total_tv = (TextView) findViewById(R.id.total_tv);
		confirm_to_pay = (Button) findViewById(R.id.confirm_to_pay);
		total_tv.setText("合计：￥"+ CartFragment.totalSum);
		CartUtil.addToConfirmList(confirmList);
		mAdapter = new ConfirmToPayListViewAdapter(this, inflater, confirmList, total_tv);
		content_lv.setAdapter(mAdapter);
		confirm_to_pay.setOnClickListener(this);
	}
	
	private void toPay(){
		if(rb_weixin.isChecked()){
			
		}else if(rb_zhifubao.isChecked()){
			
		}else{
			ToastUtil.diaplayMesShort(this, "请选择支付方式");
		}
	}
	
	private void paySuccee(){
		CartFragment.cartList.removeAll(confirmList);
	}
	
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch(v.getId()){
		case R.id.confirm_to_pay:
			toPay();
			break;
		}
	}
	
}

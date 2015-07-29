package com.robo.store;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.Header;

import android.os.Bundle;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gc.materialdesign.views.ProgressBarCircularIndeterminate;
import com.robo.store.HomeFragment.MyOnPageChangeListener;
import com.robo.store.adapter.ImagePagerAdapter;
import com.robo.store.adapter.ImagePagerUrlAdapter;
import com.robo.store.dao.GetSingleGoodsResponse;
import com.robo.store.dao.GoodsPic;
import com.robo.store.http.HttpParameter;
import com.robo.store.http.RoboHttpClient;
import com.robo.store.http.TextHttpResponseHandler;
import com.robo.store.util.CartUtil;
import com.robo.store.util.KeyUtil;
import com.robo.store.util.LogUtil;
import com.robo.store.util.ResultParse;
import com.robo.store.util.TimeUtil;
import com.robo.store.util.ToastUtil;
import com.robo.store.util.ViewUtil;
import com.robo.store.view.AutoScrollViewPager;

public class GoodsDetailActivity extends BaseActivity implements View.OnClickListener{

	private FrameLayout back_btn_cover;
	private LinearLayout goods_layout;
	private AutoScrollViewPager auto_view_pager;
	private LinearLayout viewpager_dot_layout;
	private TextView good_price_new,good_price_old,good_newprice_end;
	private TextView good_name,good_des;
	private ImageView minus_img,plus_img;
	private TextView number_txt,goods_spec;
	private TextView good_msg;
	
	private FrameLayout add_to_cart_cover,buy_now_cover;
	private TextView get_goods_date;
	private int number = 1;
	
	private String goodsBarcode;
	private GetSingleGoodsResponse mSingleGoods;
	private ProgressBarCircularIndeterminate progressbar_m;
	private int imageSize;
	
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
		goods_layout = (LinearLayout) findViewById(R.id.goods_layout);
		progressbar_m = (ProgressBarCircularIndeterminate) findViewById(R.id.progressbar_m);
		auto_view_pager = (AutoScrollViewPager) findViewById(R.id.auto_view_pager);
		viewpager_dot_layout = (LinearLayout) findViewById(R.id.viewpager_dot_layout);
		good_price_new = (TextView) findViewById(R.id.good_price_new);
		good_price_old = (TextView) findViewById(R.id.good_price_old);
		good_newprice_end = (TextView) findViewById(R.id.good_newprice_end);
		get_goods_date = (TextView) findViewById(R.id.get_goods_date);
		
		good_name = (TextView) findViewById(R.id.good_name);
		good_des = (TextView) findViewById(R.id.good_des);
		
		minus_img = (ImageView) findViewById(R.id.minus_img);
		plus_img = (ImageView) findViewById(R.id.plus_img);
		number_txt = (TextView) findViewById(R.id.number_txt);
		goods_spec = (TextView) findViewById(R.id.goods_spec);
		
		good_msg = (TextView) findViewById(R.id.good_msg);
		add_to_cart_cover = (FrameLayout) findViewById(R.id.add_to_cart_cover);
		buy_now_cover = (FrameLayout) findViewById(R.id.buy_now_cover);
		
		number_txt.setText(String.valueOf(number));
		
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
		if(TextUtils.isEmpty(mGetSingleGoodsResponse.getVipEndDate())){
			good_price_old.setVisibility(View.GONE);
			get_goods_date.setVisibility(View.GONE);
		}else{
			String vipDays = TimeUtil.compareDate(mGetSingleGoodsResponse.getVipEndDate() + " " +TimeUtil.OneDay);
			good_newprice_end.setText("优惠价格还有" + vipDays +"结束");
			String getGoodsEndTime = TimeUtil.customFormatDate(mGetSingleGoodsResponse.getVipEndDate(), TimeUtil.Day2Format, TimeUtil.MonthFormat2);
			get_goods_date.setText("请在" + getGoodsEndTime + "前取货");
		}
		good_name.setText(mGetSingleGoodsResponse.getGoodsName());
		good_msg.setText(mGetSingleGoodsResponse.getGoodsMemo());
		goods_spec.setText(mGetSingleGoodsResponse.getGoodsSpec());
	}
	
	private void RequestData(){
		goods_layout.setVisibility(View.GONE);
		progressbar_m.setVisibility(View.VISIBLE);
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("goodsBarcode", goodsBarcode);
		params.put("cityId", HomeFragment.cityId);
		RoboHttpClient.post(HttpParameter.goodUrl, "getSingleGoods", params, new TextHttpResponseHandler(){

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
				showEmptyLayout_Error();
				ToastUtil.diaplayMesLong(GoodsDetailActivity.this, GoodsDetailActivity.this.getResources().getString(R.string.connet_fail));
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, String result) {
				LogUtil.DefalutLog(result);
				mSingleGoods = (GetSingleGoodsResponse) ResultParse.parseResult(result,GetSingleGoodsResponse.class);
				if(ResultParse.handleResutl(GoodsDetailActivity.this, mSingleGoods)){
					goods_layout.setVisibility(View.VISIBLE);
					mSingleGoods.setGoodsBarcode(goodsBarcode);
					if(TextUtils.isEmpty(mSingleGoods.getGoodsName())){
						showEmptyLayout_Empty();
					}else{
						setData(mSingleGoods);
						if(mSingleGoods.getPicList().size() > 0){
							setAutoScrollViewPager(mSingleGoods.getPicList());
						}
					}
				}else{
					showEmptyLayout_Empty();
				}
			}
			
			@Override
			public void onFinish() {
				progressbar_m.setVisibility(View.GONE);
			}
		});
	}
	
	private void setAutoScrollViewPager(List<GoodsPic> picList){
		ArrayList<String> imageIdList = new ArrayList<String>();
		for(GoodsPic mGoodsPic : picList){
			imageIdList.add(mGoodsPic.getPicUrl());
		}
		imageSize = imageIdList.size();
		auto_view_pager.setAdapter(new ImagePagerUrlAdapter(this, imageIdList).setInfiniteLoop(true));
        auto_view_pager.setOnPageChangeListener(new MyOnPageChangeListener());
        if(imageSize > 1){
        	auto_view_pager.setInterval(2000);
        	auto_view_pager.startAutoScroll();
        }
        int currentItem = Integer.MAX_VALUE / 2 - Integer.MAX_VALUE / 2 % imageIdList.size();
        auto_view_pager.setCurrentItem(currentItem);
        for(int i=0;i<imageIdList.size();i++){
        	viewpager_dot_layout.addView( ViewUtil.getDot(this,i) );
        }
        ViewUtil.changeState(viewpager_dot_layout, currentItem%imageSize);
	}
	
	public class MyOnPageChangeListener implements OnPageChangeListener {

        @Override
        public void onPageSelected(int position) {
        	ViewUtil.changeState(viewpager_dot_layout, (position%imageSize));
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

        @Override
        public void onPageScrollStateChanged(int arg0) {}
    }
	
	@Override
	public void onClickEmptyLayoutRefresh() {
		RequestData();
	}
	
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch(v.getId()){
		case R.id.back_btn_cover:
			GoodsDetailActivity.this.finish();
			break;
		case R.id.minus_img:
			if(number > 1){
				number--;
			}
			number_txt.setText(String.valueOf(number));
			break;
		case R.id.plus_img:
			number++;
			number_txt.setText(String.valueOf(number));
			break;
		case R.id.add_to_cart_cover:
			ToastUtil.diaplayMesShort(GoodsDetailActivity.this, mSingleGoods.getGoodsName()+"已加入购物车");
			CartUtil.addToCart(mSingleGoods, number);
			break;
		case R.id.buy_now_cover:
			CartUtil.addToCart(mSingleGoods, number);
			GoodsDetailActivity.this.finish();
			MainActivity.setCurrentTab(2);
			break;
		}
	}
	
	
}

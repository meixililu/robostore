package com.robo.store;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.robo.store.adapter.CartListViewAdapter;
import com.robo.store.dao.GoodsBase;
import com.robo.store.util.CartUtil;
import com.robo.store.util.KeyUtil;
import com.robo.store.util.LogUtil;
import com.robo.store.util.LoginUtil;
import com.robo.store.util.SaveData;
import com.robo.store.util.ToastUtil;

public class CartFragment extends BaseFragment implements OnClickListener{

	public static final String SaveCartDataName = "CartListData";
	
	public static List<GoodsBase> cartList;
	private FrameLayout edit_cover;
	private LinearLayout message_layout,cart_layout;
	private TextView edit;
	private ListView content_lv;
	private CheckBox check_all;
	private TextView total_tv;
	private Button btn_to_balance_or_delete;
	private boolean isEdit;//true is edit
	private CartListViewAdapter mAdapter;
	private LayoutInflater inflater;
	
	public static double totalSum;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		cartList = new ArrayList<GoodsBase>();
		getCacheData();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		LogUtil.DefalutLog(CartFragment.class.getName()+"---onCreateView");
		this.inflater = inflater;
		setLayoutId(R.layout.fragment_cart);
        return super.onCreateView(inflater, container, savedInstanceState);
	}

	protected void initView(){
		edit_cover = (FrameLayout) getView().findViewById(R.id.edit_cover);
		message_layout = (LinearLayout) getView().findViewById(R.id.message_layout);
		cart_layout = (LinearLayout) getView().findViewById(R.id.cart_layout);
		edit = (TextView) getView().findViewById(R.id.edit);
		content_lv = (ListView) getView().findViewById(R.id.content_lv);
		check_all = (CheckBox) getView().findViewById(R.id.check_all);
		total_tv = (TextView) getView().findViewById(R.id.total);
		btn_to_balance_or_delete = (Button) getView().findViewById(R.id.btn_to_balance_or_delete);
		
		mAdapter = new CartListViewAdapter(getActivity(), inflater, cartList,total_tv,check_all);
		content_lv.setAdapter(mAdapter);
		edit_cover.setOnClickListener(this);
		check_all.setOnClickListener(this);
		message_layout.setOnClickListener(this);
		btn_to_balance_or_delete.setOnClickListener(this);
	}
	
	private void getCacheData(){
		try {
			Object mObject = SaveData.getObject(getActivity(), SaveCartDataName);
			if(mObject != null){
				cartList = (ArrayList<GoodsBase>) mObject;
			}
		} catch (Exception e) {
			cartList = new ArrayList<GoodsBase>();
			e.printStackTrace();
		}
	}
	
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if(isVisibleToUser){
			checkIsCartEmpty();
			if(mAdapter != null){
				mAdapter.notifyDataSetChanged();
			}
			if(total_tv != null && check_all != null){
				CartUtil.setTotalSum(total_tv,check_all);
			}
		}
	}
	
	private void checkIsCartEmpty(){
		if(cartList.size() == 0){
			message_layout.setVisibility(View.VISIBLE);
			edit_cover.setVisibility(View.GONE);
			cart_layout.setVisibility(View.GONE);
		}else{
			cart_layout.setVisibility(View.VISIBLE);
			edit_cover.setVisibility(View.VISIBLE);
			message_layout.setVisibility(View.GONE);
		}
	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.edit_cover:
			setEditStatus();
			break;
		case R.id.check_all:
			CartUtil.selectAllOrNot(check_all.isChecked());
			mAdapter.notifyDataSetChanged();
			CartUtil.setTotalSum(total_tv);
			break;
		case R.id.message_layout:
			MainActivity.setCurrentTab(0);
			break;
		case R.id.btn_to_balance_or_delete:
			toBalanceOrDelete();
			break;
		}
	}
	
	private void setEditStatus(){
		isEdit = !isEdit;
		if(isEdit){
			total_tv.setVisibility(View.GONE);
			edit.setText("完成");
			edit.setBackground(this.getResources().getDrawable(R.color.none));
			btn_to_balance_or_delete.setText("删除");
			btn_to_balance_or_delete.setTextColor(getActivity().getResources().getColor(R.color.red));
		}else{
			checkIsCartEmpty();
			CartUtil.setTotalSum(total_tv);
			total_tv.setVisibility(View.VISIBLE);
			edit.setText("  ");
			edit.setBackground(this.getResources().getDrawable(R.drawable.edit));
			btn_to_balance_or_delete.setText("去结算");
			btn_to_balance_or_delete.setTextColor(getActivity().getResources().getColor(R.color.white));
		}
	}
	
	private void toBalanceOrDelete(){
		if(isEdit){
			CartUtil.delete();
			mAdapter.notifyDataSetChanged();
		}else{
			if(LoginUtil.isLogin){
				if(CartFragment.totalSum > 0){
					toActivity(ConfirmOrderActivity.class, null);
				}else{
					ToastUtil.diaplayMesShort(getActivity(), "请至少选择一个商品");
				}
			}else{
				Bundle mBundle = new Bundle();
				mBundle.putSerializable(KeyUtil.ToClass, ConfirmOrderActivity.class);	
				toActivity(LoginActivity.class, mBundle);
			}
		}
	}
	
	@Override
	public void onDestroyView() {
		LogUtil.DefalutLog(CartFragment.class.getName()+"---onDestroyView");
		super.onDestroyView();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		if(cartList != null){
			SaveData.saveObject(getActivity(), SaveCartDataName, cartList);
			cartList.clear();
			cartList = null;
		}
		totalSum = 0;
	}
}

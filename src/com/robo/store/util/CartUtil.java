package com.robo.store.util;

import java.util.ArrayList;
import java.util.List;

import android.widget.TextView;

import com.robo.store.CartFragment;
import com.robo.store.dao.GetSingleGoodsResponse;
import com.robo.store.dao.GoodsBase;

public class CartUtil {

	public static void addToCart(GetSingleGoodsResponse mSingleGoods, int num){
		GoodsBase mGoodsBase = new GoodsBase();
		mGoodsBase.setGoodsBarcode(mSingleGoods.getGoodsBarcode());
		mGoodsBase.setGoodsName(mSingleGoods.getGoodsName());
		mGoodsBase.setRetailPrice(mSingleGoods.getRetailPrice());
		mGoodsBase.setVipPrice(mSingleGoods.getVipPrice());
		mGoodsBase.setNumber(num);
		if(!compareGoodsList(mGoodsBase)){
			CartFragment.cartList.add(mGoodsBase);
		}
	}
	
	public static void addToCart(GoodsBase mGoods, int num){
		GoodsBase mGoodsBase = new GoodsBase();
		mGoodsBase.setGoodsBarcode(mGoods.getGoodsBarcode());
		mGoodsBase.setGoodsName(mGoods.getGoodsName());
		mGoodsBase.setRetailPrice(mGoods.getRetailPrice());
		mGoodsBase.setVipPrice(mGoods.getVipPrice());
		mGoodsBase.setNumber(num);
		if(!compareGoodsList(mGoodsBase)){
			CartFragment.cartList.add(mGoodsBase);
		}
	}
	
	public static void addToConfirmList(List<GoodsBase> confirmList){
		for(GoodsBase goods : CartFragment.cartList){
			if(goods.isSelected()){
				confirmList.add(goods);
			}
		}
	}
	
	public static boolean compareGoodsList(GoodsBase mGoodsBase){
		boolean isAdd = false;
		for(GoodsBase old : CartFragment.cartList){
			if(old.getGoodsBarcode().equals(mGoodsBase.getGoodsBarcode())){
				old.setNumber(old.getNumber() + mGoodsBase.getNumber());;
				isAdd = true;
			}
		}
		return isAdd;
	}
	
	public static void selectAllOrNot(boolean isSelectAll){
		for(GoodsBase goods : CartFragment.cartList){
			goods.setSelected(isSelectAll);
		}
	}
	
	public static void delete(){
		List<GoodsBase> deleteList = new ArrayList<GoodsBase>();
		for(GoodsBase goods : CartFragment.cartList){
			if(goods.isSelected()){
				deleteList.add(goods);
			}
		}
		CartFragment.cartList.removeAll(deleteList);
	}
	
	public static void setTotalSum(TextView total_tv){
		if(CartFragment.cartList != null){
			CartFragment.totalSum = 0;
			for(GoodsBase goods : CartFragment.cartList){
				if(goods.isSelected()){
					double privace = NumberUtil.StringToDouble(goods.getVipPrice()) * goods.getNumber();
					CartFragment.totalSum += privace;
				}
			}
//			DecimalFormat df = new DecimalFormat("#.##");
			total_tv.setText("合计：￥"+ CartFragment.totalSum);
		}
	}
	
}

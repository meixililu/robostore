package com.robo.store.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.robo.store.R;
import com.robo.store.ShopLocationFragment;
import com.robo.store.ShopMemoFragment;

public class ShopActivityPagerAdapter extends FragmentPagerAdapter {

	public static String[] CONTENT;
	
    public ShopActivityPagerAdapter(FragmentManager fm, Context mContext) {
        super(fm);
        CONTENT = new String[] { 
        		mContext.getResources().getString(R.string.map_location),
        		mContext.getResources().getString(R.string.shop_des),
        };
    }

    @Override
    public Fragment getItem(int position) {
        if( position == 0 ){
        	return new ShopLocationFragment();
        }else if( position == 1 ){
        	return new ShopMemoFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return CONTENT.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return CONTENT[position].toUpperCase();
    }
}
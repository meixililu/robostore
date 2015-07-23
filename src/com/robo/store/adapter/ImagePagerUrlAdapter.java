package com.robo.store.adapter;

/*
 * Copyright 2014 trinea.cn All right reserved. This software is the confidential and proprietary information of
 * trinea.cn ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with trinea.cn.
 */
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.robo.store.view.RecyclingPagerAdapter;
import com.squareup.picasso.Picasso;


/**
 * ImagePagerAdapter
 * 
 * @author <a href="http://www.trinea.cn" target="_blank">Trinea</a> 2014-2-23
 */
public class ImagePagerUrlAdapter extends RecyclingPagerAdapter {

    private Context       context;
    private List<String> imageIdList;

    private int           size;
    private boolean       isInfiniteLoop;

    public ImagePagerUrlAdapter(Context context, List<String> imageIdList) {
        this.context = context;
        this.imageIdList = imageIdList;
        this.size = imageIdList.size();
        isInfiniteLoop = false;
    }

    @Override
    public int getCount() {
        // Infinite loop
        return isInfiniteLoop ? Integer.MAX_VALUE : imageIdList.size();
    }

    /**
     * get really position
     * 
     * @param position
     * @return
     */
    private int getPosition(int position) {
        return isInfiniteLoop ? position % size : position;
    }

    @Override
    public View getView(int position, View view, ViewGroup container) {
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = holder.imageView = new ImageView(context);
            holder.imageView.setScaleType(ScaleType.CENTER_CROP);
            view.setTag(holder);
        } else {
            holder = (ViewHolder)view.getTag();
        }
        int RealPosition = getPosition(position);
        String url = imageIdList.get(RealPosition);
        try {
			Picasso.with(context)
			.load(url)
			.tag(context)
			.into(holder.imageView);
		} catch (Exception e) {
			e.printStackTrace();
		}
        return view;
    }

    private static class ViewHolder {

        ImageView imageView;
    }

    /**
     * @return the isInfiniteLoop
     */
    public boolean isInfiniteLoop() {
        return isInfiniteLoop;
    }

    /**
     * @param isInfiniteLoop the isInfiniteLoop to set
     */
    public ImagePagerUrlAdapter setInfiniteLoop(boolean isInfiniteLoop) {
        this.isInfiniteLoop = isInfiniteLoop;
        return this;
    }
}
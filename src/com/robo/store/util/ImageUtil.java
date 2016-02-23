package com.robo.store.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class ImageUtil {
	
	public static String shutCutImg = "@1e_211w_185h_0c_0i_1o_1x.jpg";
	public static String shutCutImg1 = "@1e_1280w_800h_0c_0i_1o_1x.jpg";
	
	public static Bitmap getdecodeBitmap(String img){
		return decodeFile(new File(img));
	}

	//decodes image and scales it to reduce memory consumption
    public static Bitmap decodeFile(File f){
        try {
            //decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f),null,o);
            
            //Find the correct scale value. It should be the power of 2.
            int width_tmp=o.outWidth, height_tmp=o.outHeight;
            int scale=1;
			 while(true){
				 if(width_tmp/2 < 600 || height_tmp/2 < 600)
					 break;
				 width_tmp/=2;
				 height_tmp/=2;
				 scale*=2;
			 }
            
            //decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {
        	return null;
        }
    }
	
}

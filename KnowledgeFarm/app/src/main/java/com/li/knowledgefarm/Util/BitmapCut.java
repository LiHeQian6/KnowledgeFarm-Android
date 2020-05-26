package com.li.knowledgefarm.Util;

import android.graphics.Bitmap;

/**
 * Created by can on 2017/08/11.
 * 图片裁剪类
 */
 
public class BitmapCut {
 
    /**
     * 裁剪图片
     */
    public static Bitmap cutBitmap(Bitmap bm,int num,int sum){
        Bitmap bitmap = null;
        if(bm!=null){
            bitmap = Bitmap.createBitmap(bm,num*bm.getWidth()/sum,0,bm.getWidth()/sum,bm.getHeight()); //对图片的高度的一半进行裁剪
        }
        return bitmap;
    }
 
}
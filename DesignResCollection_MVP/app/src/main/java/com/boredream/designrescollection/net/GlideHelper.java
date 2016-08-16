package com.boredream.designrescollection.net;


import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.boredream.bdcodehelper.net.GlideCircleTransform;
import com.boredream.bdcodehelper.net.RoundedCornersTransformation;
import com.boredream.designrescollection.R;
import com.bumptech.glide.BitmapRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;

/**
 * Glide装饰器
 */
public class GlideHelper {

    public static void showAvatar(Context context, String avatar, ImageView iv) {
        Glide.with(context)
                .load(avatar)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.mipmap.ic_account_circle_grey600_24dp)
                .error(R.mipmap.ic_account_circle_grey600_24dp)
                .transform(new GlideCircleTransform(context))
                .crossFade()
                .into(iv);
    }

    public static void showImage(Context context, String imageUrl, ImageView iv) {
        BitmapRequestBuilder<String, Bitmap> requestBuilder = Glide.with(context)
                .load(imageUrl)
                .asBitmap()
                .animate(android.R.anim.fade_in)
                .diskCacheStrategy(DiskCacheStrategy.ALL);

        if (iv.getScaleType() == ImageView.ScaleType.CENTER_CROP) {
            requestBuilder.centerCrop();
        } else {
            requestBuilder.fitCenter();
        }

        requestBuilder.into(iv);
    }

    public static void showCornerImage(Context context, String imageUrl, ImageView iv, int corner) {
        BitmapRequestBuilder<String, Bitmap> requestBuilder = Glide.with(context)
                .load(imageUrl)
                .asBitmap()
                .transform(new CenterCrop(context),
                        new RoundedCornersTransformation(context, corner, 0))
                .animate(android.R.anim.fade_in)
                .diskCacheStrategy(DiskCacheStrategy.ALL);

        requestBuilder.into(iv);
    }
}

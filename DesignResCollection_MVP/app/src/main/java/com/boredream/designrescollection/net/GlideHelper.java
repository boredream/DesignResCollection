package com.boredream.designrescollection.net;


import android.content.Context;
import android.widget.ImageView;

import com.boredream.bdcodehelper.net.GlideCircleTransform;
import com.boredream.designrescollection.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

/**
 * Glide
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
        Glide.with(context)
                .load(imageUrl)
                .asBitmap()
                .animate(android.R.anim.fade_in)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(iv);
    }
}

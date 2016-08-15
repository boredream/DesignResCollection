package com.boredream.bdcodehelper.adapter;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.boredream.bdcodehelper.R;
import com.boredream.bdcodehelper.entity.ImageUrlInterface;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.util.List;

import uk.co.senab.photoview.PhotoViewAttacher;

public class ImageBrowserAdapter extends PagerAdapter {

    private Activity context;
    private List<? extends ImageUrlInterface> picUrls;

    public ImageBrowserAdapter(Activity context, List<? extends ImageUrlInterface> picUrls) {
        this.context = context;
        this.picUrls = picUrls;
    }

    @Override
    public int getCount() {
        if (picUrls.size() > 1) {
            return Integer.MAX_VALUE;
        }
        return picUrls.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public View instantiateItem(final ViewGroup container, int position) {
        final View rootView = View.inflate(context, R.layout.item_image_browser, null);

        int index = position % picUrls.size();
        final ProgressBar pb_loading = (ProgressBar) rootView.findViewById(R.id.pb_loading);
        final ImageView iv_image_browser = (ImageView) rootView.findViewById(R.id.iv_image_browser);
        final PhotoViewAttacher pva = new PhotoViewAttacher(iv_image_browser);
        String url = picUrls.get(index).getImageUrl();

        Glide.with(context)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .placeholder(R.mipmap.ic_account_circle_grey600_24dp)
//                .error(R.mipmap.ic_account_circle_grey600_24dp)
                .centerCrop()
                .crossFade()
                .into(new SimpleTarget<GlideDrawable>() {

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        super.onLoadFailed(e, errorDrawable);
                        pb_loading.setVisibility(View.GONE);
                        iv_image_browser.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onLoadStarted(Drawable placeholder) {
                        super.onLoadStarted(placeholder);
                        pb_loading.setVisibility(View.VISIBLE);
                        iv_image_browser.setVisibility(View.GONE);
                    }

                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                        pb_loading.setVisibility(View.GONE);
                        iv_image_browser.setVisibility(View.VISIBLE);

                        iv_image_browser.setImageDrawable(resource);
                        pva.update();
                    }
                });

        pva.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float x, float y) {
                context.onBackPressed();
            }
        });

        container.addView(rootView);
        return rootView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
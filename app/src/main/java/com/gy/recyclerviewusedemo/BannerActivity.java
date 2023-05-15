package com.gy.recyclerviewusedemo;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.youth.banner.Banner;
import com.youth.banner.indicator.CircleIndicator;
import com.zhouwei.mzbanner.MZBannerView;
import com.zhouwei.mzbanner.holder.MZHolderCreator;
import com.zhouwei.mzbanner.holder.MZViewHolder;

import java.util.ArrayList;
import java.util.List;

public class BannerActivity extends AppCompatActivity {

    private Banner banner;
    private MZBannerView mMZBanner;
    private List<ImageBean> imgs = new ArrayList<>();
    private String [] imgUrls = {
    "https://mapopen-website-wiki.bj.bcebos.com/banner/webreset-banner.jpg"
    ,"https://mapopen-website-wiki.bj.bcebos.com/solutions/scheduling/scheduling-banner2.jpg"
    ,"https://mapopen-website-wiki.cdn.bcebos.com/banner/lightauthbanner.png"
    ,"https://mapopen-website-wiki.cdn.bcebos.com/banner/new_nav_sdk.png"
    ,"https://mapopen-website-wiki.cdn.bcebos.com/banner/smart_traffic_light%402.png"
    ,"https://img-operation.csdnimg.cn/csdn/silkroad/img/1683622576466.png"
    ,"https://img-operation.csdnimg.cn/csdn/silkroad/img/1683770810899.png"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner);
        initView();
    }

    private void initView() {
        banner = findViewById(R.id.banner);
        mMZBanner = findViewById(R.id.mzBanner);

        for (int i = 0; i < imgUrls.length; i++) {
            imgs.add(new ImageBean(imgUrls[i]));
        }

        banner.addBannerLifecycleObserver(this)
                .setAdapter(new ImageAdapter(imgs))
                .setIndicator(new CircleIndicator(this))
                .setBannerGalleryEffect(15,0)
                .setLoopTime(2000);

        // 设置数据
        mMZBanner.setPages(imgs, new MZHolderCreator<BannerViewHolder>() {
            @Override
            public BannerViewHolder createViewHolder() {
                return new BannerViewHolder();
            }
        });
        //设置是否显示Indicator
        mMZBanner.setIndicatorVisible(true);
        mMZBanner.setDelayedTime(2000);
    }

    class BannerViewHolder implements MZViewHolder<ImageBean> {
        private ImageView mImageView;
        @Override
        public View createView(Context context) {
            // 返回页面布局
            View view = LayoutInflater.from(context).inflate(R.layout.banner_item,null);
            mImageView = view.findViewById(R.id.banner_image);
            return view;
        }

        @Override
        public void onBind(Context context, int position, ImageBean data) {
            // 数据绑定
            Glide.with(getBaseContext()).load(data.getImg()).into(mImageView);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        //开始轮播
        banner.start();
        mMZBanner.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //结束轮播
        banner.stop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //暂停MZBannerView轮播
        mMZBanner.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //销毁轮播
        banner.destroy();
    }

}
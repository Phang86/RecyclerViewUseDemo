package com.gy.recyclerviewusedemo.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.gy.recyclerviewusedemo.adapter.ImageAdapter;
import com.gy.recyclerviewusedemo.bean.ImageBean;
import com.gy.recyclerviewusedemo.R;
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
    "https://img.alicdn.com/imgextra/i3/O1CN01Sf6dER1zbJ3uVQ0lE_!!6000000006732-0-tps-846-472.jpg"
    ,"https://img.alicdn.com/imgextra/i2/O1CN01XZC8Dd1IUs179sweg_!!6000000000897-2-tps-846-472.png"
    ,"https://img.alicdn.com/imgextra/i3/O1CN01RbnhFd1wBFigMUNYq_!!6000000006269-2-tps-846-472.png"
    ,"https://img.alicdn.com/imgextra/i1/O1CN01YGUXOM1k5VKBPo5J3_!!6000000004632-2-tps-846-472.png"
    ,"https://gw.alicdn.com/imgextra/i4/O1CN01a71ilU1T2RCTvUXDY_!!6000000002324-0-tps-846-472.jpg"
    ,"https://img.alicdn.com/imgextra/i1/O1CN01oMX2yU21pclJPCGXx_!!6000000007034-2-tps-1316-736.png"
    ,"https://img.alicdn.com/imgextra/i1/O1CN01a7jyR91cXt0ENd6hO_!!6000000003611-2-tps-1316-736.png"
    ,"https://img.alicdn.com/imgextra/i2/O1CN01X8SenW1KpbFlCrDpy_!!6000000001213-2-tps-1316-736.png"
    ,"https://img.alicdn.com/imgextra/i2/O1CN01samGeP1RB1PEiilFO_!!6000000002072-0-tps-1316-736.jpg"
    ,"https://img30.360buyimg.com/pop/s1180x940_jfs/t1/93973/27/37836/99196/6459b114F7ca5b000/80a89e0ef91c5bf5.jpg"
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
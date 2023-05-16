package com.gy.recyclerviewusedemo.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.gy.recyclerviewusedemo.bean.ImageBean;
import com.youth.banner.adapter.BannerAdapter;

import java.util.List;

public class ImageAdapter extends BannerAdapter<ImageBean, ImageAdapter.BannerViewHolder> {
    Context context;

    public ImageAdapter(List<ImageBean> datas) {
        super(datas);
    }

    @Override
    public ImageAdapter.BannerViewHolder onCreateHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        ImageView imageView = new ImageView(parent.getContext());
        //注意，必须设置为match_parent，这个是viewpager2强制要求的
        imageView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return new BannerViewHolder(imageView);
    }

    @Override
    public void onBindView(ImageAdapter.BannerViewHolder holder, ImageBean data, int position, int size) {
//        holder.imageView.setImageResource(data.getImg());
        Glide.with(context).load(data.getImg()).into(holder.imageView);
    }

    public class BannerViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageView;

        public BannerViewHolder(@NonNull ImageView itemView) {
            super(itemView);
            this.imageView = itemView;
        }
    }
}

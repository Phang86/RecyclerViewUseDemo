package com.gy.recyclerviewusedemo.adapter;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.gy.recyclerviewusedemo.R;
import com.gy.recyclerviewusedemo.bean.CarResponse;

import java.util.List;

public class GoodsAdapter extends BaseQuickAdapter<CarResponse.OrderDataBean.CartlistBean, BaseViewHolder> {

    public GoodsAdapter(int layoutResId, List<CarResponse.OrderDataBean.CartlistBean> datas) {
        super(layoutResId, datas);
    }

    private void testTest(){

    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, CarResponse.OrderDataBean.CartlistBean cartlistBean) {
        baseViewHolder.setText(R.id.tv_good_name, cartlistBean.getProductName())
                .setText(R.id.tv_good_color,cartlistBean.getColor())
                .setText(R.id.tv_goods_price,cartlistBean.getPrice()+"")
                .setText(R.id.tv_goods_num,cartlistBean.getCount()+"")
                .setText(R.id.tv_good_size,cartlistBean.getSize());
        ImageView iv = baseViewHolder.getView(R.id.iv_goods);
        Glide.with(getContext()).load(cartlistBean.getDefaultPic()).into(iv);
        ImageView checkedGoods = baseViewHolder.getView(R.id.iv_checked_goods);
        //判断商品是否选中
        if (cartlistBean.isChecked()) {
            checkedGoods.setImageDrawable(getContext().getDrawable(R.drawable.ic_checked));
        } else {
            checkedGoods.setImageDrawable(getContext().getDrawable(R.drawable.ic_check));
        }
    }
}

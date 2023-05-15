package com.gy.recyclerviewusedemo;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import java.util.List;

public class DataAdapter extends BaseQuickAdapter<DataResponse.DataBean, BaseViewHolder> {

    private static final int STATE_DEFAULT = 0;//默认状态
    int mEditMode = STATE_DEFAULT;

    public DataAdapter(int layoutResId, List<DataResponse.DataBean> list) {
        super(layoutResId,list);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, DataResponse.DataBean dataBean) {
        //Glide加载网络图片
        Glide.with(getContext()).load(dataBean.getUrl()).into((ImageView) baseViewHolder.getView(R.id.iv_img));
        baseViewHolder.setText(R.id.tv_video_info,dataBean.getAuthor());
        if (mEditMode == STATE_DEFAULT) {
            //默认不显示
            baseViewHolder.getView(R.id.iv_check).setVisibility(View.GONE);
        } else {
            //显示   显示之后再做点击之后的判断
            baseViewHolder.getView(R.id.iv_check).setVisibility(View.VISIBLE);
            if (dataBean.isSelect()) {
                //点击时，true 选中
                baseViewHolder.getView(R.id.iv_check).setBackgroundResource(R.mipmap.icon_choose_selected);
            } else {//false 取消选中
                baseViewHolder.getView(R.id.iv_check).setBackgroundResource(R.mipmap.icon_choose_default);
            }
        }
    }

    /**
     * 设置编辑状态   接收Activity中传递的值，并改变Adapter的状态
     */
    public void setEditMode(int editMode) {
        mEditMode = editMode;
        notifyDataSetChanged();//刷新
    }

}

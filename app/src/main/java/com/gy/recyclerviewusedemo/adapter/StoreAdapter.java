package com.gy.recyclerviewusedemo.adapter;


import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.gy.recyclerviewusedemo.R;
import com.gy.recyclerviewusedemo.bean.CarResponse;
import com.gy.recyclerviewusedemo.util.GoodsCallback;

import java.util.List;

import javax.xml.namespace.QName;

public class StoreAdapter extends BaseQuickAdapter<CarResponse.OrderDataBean, BaseViewHolder> {

    private RecyclerView rv;
    //商品回调
    private GoodsCallback goodsCallback;
    //店铺对象
    private List<CarResponse.OrderDataBean> storeBean;


    public StoreAdapter(int layoutResId, List<CarResponse.OrderDataBean> dataBeans, GoodsCallback callback) {
        super(layoutResId,dataBeans);
        this.goodsCallback = callback;
        storeBean = dataBeans; //赋值
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, CarResponse.OrderDataBean orderDataBean) {
        rv = baseViewHolder.getView(R.id.rv_goods);
        baseViewHolder.setText(R.id.tv_store_name,orderDataBean.getShopName());
        ImageView checkedStore = baseViewHolder.getView(R.id.iv_checked_store);
        if (orderDataBean.isChecked()) {
            checkedStore.setImageDrawable(getContext().getDrawable(R.drawable.ic_checked));
        } else {
            checkedStore.setImageDrawable(getContext().getDrawable(R.drawable.ic_check));
        }
        //点击事件
        addChildClickViewIds(R.id.iv_checked_store);//选中店铺

        GoodsAdapter goodsAdapter = new GoodsAdapter(R.layout.item_good, orderDataBean.getCartlist());
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setAdapter(goodsAdapter);
        //商品item中的点击事件
        goodsAdapter.addChildClickViewIds(R.id.iv_checked_goods);
        goodsAdapter.addChildClickViewIds(R.id.tv_increase_goods_num);
        goodsAdapter.addChildClickViewIds(R.id.tv_reduce_goods_num);
        goodsAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
                CarResponse.OrderDataBean.CartlistBean bean = orderDataBean.getCartlist().get(position);

                switch (view.getId()) {
                    case R.id.iv_checked_goods:
                        //如果已选中则取消选中，未选中则选中
                        bean.setChecked(!bean.isChecked());
                        //刷新适配器
                        goodsAdapter.notifyDataSetChanged();
                        //控制店铺是否选中
                        controlStore(orderDataBean);
                        //商品控制价格
                        goodsCallback.calculationPrice();
                        break;
                    case R.id.tv_increase_goods_num:
                        //增加商品数量
                        updateGoodsNum(bean,goodsAdapter,true);
                        break;
                    case R.id.tv_reduce_goods_num:
                        //减少商品数量
                        updateGoodsNum(bean,goodsAdapter,false);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    /**
     * 控制店铺是否选中
     */
    private void controlStore(CarResponse.OrderDataBean item) {
        int num = 0;
        for (CarResponse.OrderDataBean.CartlistBean bean : item.getCartlist()) {
            if (bean.isChecked()) {
                ++num;
            }
        }
        if (num == item.getCartlist().size()) {
            //全选中  传递需要选中的店铺的id过去
            goodsCallback.checkedStore(item.getShopId(),true);
        } else {
            goodsCallback.checkedStore(item.getShopId(),false);
        }
    }

    /**
     * 控制商品是否选中
     */
    public void controlGoods(int shopId, boolean state) {
        //根据店铺id选中该店铺下所有商品
        for (CarResponse.OrderDataBean orderDataBean : storeBean) {
            //店铺id等于传递过来的店铺id  则选中该店铺下所有商品
            if (orderDataBean.getShopId() == shopId) {
                for (CarResponse.OrderDataBean.CartlistBean cartlistBean : orderDataBean.getCartlist()) {
                    cartlistBean.setChecked(state);
                    //刷新
                    notifyDataSetChanged();
                }
            }
        }
    }

    /**
     * 修改商品数量  增加或者减少
     * @param goodsBean
     * @param goodsAdapter
     * @param state  true增加 false减少
     */
    private void updateGoodsNum(CarResponse.OrderDataBean.CartlistBean goodsBean, GoodsAdapter goodsAdapter,boolean state) {
        //其实商品应该还有一个库存值或者其他的限定值，我这里写一个假的库存值为10
        int inventory = 10;
        int count = goodsBean.getCount();

        if(state){
            if (count >= inventory){
                Toast.makeText(getContext(),"商品数量不可超过库存值~",Toast.LENGTH_SHORT).show();
                return;
            }
            count++;
        }else {
            if (count <= 1){
                Toast.makeText(getContext(),"已是最低商品数量~",Toast.LENGTH_SHORT).show();
                return;
            }
            count--;
        }
        goodsBean.setCount(count);//设置商品数量
        //刷新适配器
        goodsAdapter.notifyDataSetChanged();
        //计算商品价格
        goodsCallback.calculationPrice();
    }

}

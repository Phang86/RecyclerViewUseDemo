package com.gy.recyclerviewusedemo;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.gy.recyclerviewusedemo.adapter.DataAdapter;
import com.gy.recyclerviewusedemo.bean.DataResponse;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.tv_edit)
    TextView tvEdit;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rv_normal_show)
    LinearLayout rvNormalShow;
    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;
    @BindView(R.id.tv_check_all)
    TextView tvCheckAll;
    @BindView(R.id.tv_delete)
    TextView tvDelete;
    @BindView(R.id.lay_bottom)
    LinearLayout layBottom;
    private static final int STATE_DEFAULT = 0;//默认状态
    private static final int STATE_EDIT = 1;//编辑状态
    private int mEditMode = STATE_DEFAULT;
    private boolean editorStatus = false;//是否为编辑状态
    private int index = 0;//当前选中的item数

    List<DataResponse.DataBean> mList = new ArrayList<>();//列表
    DataAdapter mAdapter;//适配器

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initList();
        //禁用下拉和上拉
        refresh.setEnableRefresh(false);
        refresh.setEnableLoadMore(false);
    }

    private void initList() {
        mAdapter = new DataAdapter(R.layout.item_data_list, mList);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            if (editorStatus) {//编辑状态
                DataResponse.DataBean dataBean = mList.get(position);//赋值
                boolean isSelect = dataBean.isSelect();
                if (!isSelect) {
                    index++;
                    dataBean.setSelect(true);
                } else {
                    dataBean.setSelect(false);
                    index--;
                }
                if (index == 0) {//当前选中item数量
                    tvDelete.setText("删除");
                } else {
                    tvDelete.setText("删除(" + index + ")");
                }
                mAdapter.notifyDataSetChanged();
            }
        });

        DataResponse dataResponse = new Gson().fromJson(JsonData.JSON, DataResponse.class);
        List<DataResponse.DataBean> data = dataResponse.getData();
        if (data.size() > 0) {
            mList.clear();
            mList.addAll(data);
            mAdapter.notifyDataSetChanged(); //刷新数据
            RecyclerViewAnimation.runLayoutAnimation(rv);  //动画显示
            rv.setVisibility(View.VISIBLE);
            rvNormalShow.setVisibility(View.GONE);
        } else {
            rv.setVisibility(View.GONE);
            rvNormalShow.setVisibility(View.VISIBLE);
        }
        refresh.finishRefresh();
    }

    @OnClick({R.id.tv_edit, R.id.tv_check_all, R.id.tv_delete})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_edit:
                //编辑
                updateEditState();
                break;
            case R.id.tv_check_all:
                //全选
                setAllItemChecked();
                break;
            case R.id.tv_delete:
                //删除
                deleteCheckItem();
                break;
        }
    }

    //删除选中的item
    private void deleteCheckItem() {
        if (mAdapter == null) return;

        for (int i = mList.size() - 1; i >= 0; i--) {
            if (mList.get(i).isSelect() == true) {
                mList.remove(i);
            }
        }

        //删除选中的item之后判断是否还有数据，没有则退出编辑模式
        if (mList.size() != 0) {
            index = 0;//删除之后置为0
            tvDelete.setText("删除");
        } else {
            tvEdit.setText("编辑");
            layBottom.setVisibility(View.GONE);
            editorStatus = false;
            //没有数据自然也不存在编辑了
            tvEdit.setVisibility(View.GONE);
            rvNormalShow.setVisibility(View.VISIBLE);
            //启用下拉
            refresh.setEnableRefresh(true);
            //下拉刷新
            refresh.setOnRefreshListener(refreshLayout -> {
                //重新装填数据
                initList();
                mEditMode = STATE_DEFAULT;//恢复默认状态
                editorStatus = false;//恢复默认状态
                tvEdit.setVisibility(View.VISIBLE);//显示编辑
            });
        }
        mAdapter.notifyDataSetChanged();//刷新
        RecyclerViewAnimation.runLayoutAnimation(rv);  //动画显示
    }


    //改变编辑状态
    private void updateEditState() {
        mEditMode = mEditMode == STATE_DEFAULT ? STATE_EDIT : STATE_DEFAULT;
        if (mEditMode == STATE_EDIT) {
            tvEdit.setText("取消");
            layBottom.setVisibility(View.VISIBLE);
            editorStatus = true;  //处于编辑状态
        } else {
            tvEdit.setText("编辑");
            layBottom.setVisibility(View.GONE);
            editorStatus = false;  //处于默认状态

            setAllItemUnchecked(); //取消全选
        }
        mAdapter.setEditMode(mEditMode);//刷新Adapter
    }

    //取消全部选中
    private void setAllItemUnchecked() {
        if (mAdapter == null) return;
        for (int i = 0; i < mList.size(); i++) {
            mList.get(i).setSelect(false);
        }
        mAdapter.notifyDataSetChanged();
        tvDelete.setText("删除");
        index = 0;
    }

    //全部选中
    private void setAllItemChecked() {
        if (mAdapter == null) return;
        for (int i = 0; i < mList.size(); i++) {
            mList.get(i).setSelect(true);
        }
        mAdapter.notifyDataSetChanged();
        index = mList.size();
        tvDelete.setText("删除(" + index + ")");
    }
}
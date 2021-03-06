package com.zzti.lsy.ninetingapp.home.parts;

import android.content.Intent;
import android.view.View;

import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.base.BaseFragment;

import butterknife.OnClick;

/**
 * author：anxin on 2018/8/8 14:14
 * 配件管理员 operator 3
 */
public class PartsManagerFragment extends BaseFragment {

    public static PartsManagerFragment newInstance() {
        PartsManagerFragment fragment = new PartsManagerFragment();
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_partsmanaer;
    }

    @Override
    protected void initView() {
        tvToolbarTitle.setText("首页");
    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.rl_menu1, R.id.rl_menu2, R.id.rl_menu3, R.id.rl_menu4, R.id.rl_menu5, R.id.rl_menu6, R.id.rl_menu7})
    public void viewClick(View view) {
        switch (view.getId()) {
            case R.id.rl_menu1: //配件列表
                Intent intent = new Intent(mActivity, PartsListActivity.class);
                intent.putExtra("TAG", 2);
                startActivity(intent);
                break;
            case R.id.rl_menu2://配件采购
                startActivity(new Intent(mActivity, PartsPurchaseListActivity.class));
                break;
            case R.id.rl_menu5://日用品列表
                Intent intent7 = new Intent(mActivity, LifeGoodsListActivity.class);
                startActivity(intent7);
                break;
            case R.id.rl_menu6://日用品采购
                Intent intent4 = new Intent(mActivity, LifeGoodsPurchaseListActivity.class);
                startActivity(intent4);
                break;
        }
    }
}

package com.zzti.lsy.ninetingapp.home.generalmanager;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.base.BaseActivity;
import com.zzti.lsy.ninetingapp.home.adapter.TitleFragmentPagerAdapter;
import com.zzti.lsy.ninetingapp.home.device.BxFragment;
import com.zzti.lsy.ninetingapp.home.device.NsFragment;
import com.zzti.lsy.ninetingapp.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class ApprovalActivity extends BaseActivity {
    @BindView(R.id.tab)
    TabLayout mTabLayout;
    @BindView(R.id.viewpager)
    ViewPager mViewPager;

    @Override
    public int getContentViewId() {
        return R.layout.activity_bx_ns;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        initView();
        initData();
    }

    private void initData() {
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new PartsPurchaseListFragment());
        fragments.add(new LifeGoodsPurchaseListFragment());

        TitleFragmentPagerAdapter adapter = new TitleFragmentPagerAdapter(getSupportFragmentManager(), fragments, new String[]{"配件", "日用品"});
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);

        int tag = UIUtils.getInt4Intent(this, "TAG");
        if (tag == 0) {
            mTabLayout.getTabAt(0).select();
        } else if (tag == 1) {
            mTabLayout.getTabAt(1).select();
        }

    }

    private void initView() {

    }


    @OnClick(R.id.iv_toolbarBack)
    public void viewClick() {
        finish();
    }
}

package com.zzti.lsy.ninetingapp;

import android.support.v4.app.Fragment;

import com.zzti.lsy.ninetingapp.home.HomeFragment;
import com.zzti.lsy.ninetingapp.mine.MineFragment;
import com.zzti.lsy.ninetingapp.task.TaskFragment;

/**
 * author：anxin on 2018/8/3 16:22
 */
public class DataGenerator {

    public static Fragment[] getFragments() {
        Fragment fragments[] = new Fragment[3];
        fragments[0] = HomeFragment.newInstance();
        fragments[1] = TaskFragment.newInstance();
        fragments[2] = MineFragment.newInstance();
        return fragments;
    }


}
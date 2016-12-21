package com.wzh.fun.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * Created by zhihao.wen on 2016/4/6.
 */
public class MyViewPagerAdapter extends FragmentStatePagerAdapter {

    private List<String> titles;
    private List<Fragment> mFragments;

    public MyViewPagerAdapter(FragmentManager fm, List<String> titles, List<Fragment> mFragments) {
        super(fm);
        this.titles = titles;
        this.mFragments = mFragments;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }
}
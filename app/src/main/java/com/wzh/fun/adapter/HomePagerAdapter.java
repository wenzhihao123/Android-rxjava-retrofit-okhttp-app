package com.wzh.fun.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by zhihao.wen on 2016/11/1.
 */

public class HomePagerAdapter extends PagerAdapter {
    private ArrayList<String> urls;//tab对应的视图

    /*
    * construction Function
    *   传入view跟title数组
    * */
    public HomePagerAdapter(ArrayList<String> urls) {
        this.urls = urls;
    }

    /*
         * viewpager中组件数量
         * */
    @Override
    public int getCount() {
        return urls.size();
    }

    /*
    * 初始化item
    *   做了两件事，第一：将当前视图添加到container中，第二：返回当前View
    *
    *   这个函数的实现的功能是创建指定位置的页面视图。
    *   适配器有责任增加即将创建的View视图到这里给定的container中，
    *   这是为了确保在finishUpdate(viewGroup)返回时this is be done!

    返回值：返回一个代表新增视图页面的Object（Key），这里没必要非要返回视图本身，
        也可以这个页面的其它容器。其实我的理解是可以代表当前页面的任意值，
        只要你可以与你增加的View一一对应即可，比如position变量也可以做为Key
    * */
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView = new ImageView(container.getContext());
        Glide.with(container.getContext()).load(urls.get(position)).centerCrop().into(imageView);
        container.addView(imageView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        return imageView;
    }

    /*滑动切换的时候销毁当前组件
    *   从当前container中删除指定位置（position）的View;
    * */
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    /*
    * */
    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }

    /*
    * 该函数用来判断instantiateItem(ViewGroup, int)
    *   函数所返回来的Key与一个页面视图是否是代表的同一个视图(即它俩是否是对应的，对应的表示同一个View)
    *
    *返回值：如果对应的是同一个View，返回True，否则返回False。
    * */
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;  //官方提示这样写
    }
}

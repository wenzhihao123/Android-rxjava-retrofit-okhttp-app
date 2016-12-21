package com.wzh.fun.ui.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.wzh.fun.R;
import com.wzh.fun.adapter.HomeOnPageChangeListener;
import com.wzh.fun.adapter.HomePagerAdapter;
import com.wzh.fun.adapter.MyViewPagerAdapter;
import com.wzh.fun.view.SlidingTabLayout;
import com.wzh.fun.view.viewpager.CircleIndicator;
import com.wzh.fun.view.viewpager.LoopViewPager;

import java.util.ArrayList;

/**
 * Created by zhihao.wen on 2016/11/1.
 */

public class HomeFragment extends BaseFragment {
    private LoopViewPager pager;
    private CircleIndicator circleIndicator ;
    private ArrayList<String> urls = new ArrayList();//tab的标题
    private ArrayList<String> titles = new ArrayList();
    private ArrayList<Fragment> fragments = new ArrayList();
    private MyViewPagerAdapter mViewPagerAdapter;
    private ViewPager mViewPager ;
    private TabLayout mTabLayout;


    private NewTextJokeFragment textJokeFragment ;
    private NewImageJokeFragment imageJokeFragment ;
    private RadioGroup group ;
    private RadioButton item1 ;
    private RadioButton item2 ;
    private FragmentManager manager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        setContentView(R.layout.fragment_home);
        initView();
        return getContentView();
    }

    private void initView() {
//        pager = (LoopViewPager) findViewById(R.id.pager);
//        circleIndicator = (CircleIndicator)findViewById(R.id.indicator);
//        //从ViewPager开始添加View
//        urls.add("http://img.my.csdn.net/uploads/201407/26/1406383243_5120.jpg");
//        urls.add("http://img.my.csdn.net/uploads/201407/26/1406383248_3693.jpg");
//        urls.add("http://img.my.csdn.net/uploads/201407/26/1406383219_5806.jpg");
//        pager.setAdapter(new HomePagerAdapter(urls));
//        circleIndicator.setViewPager(pager);
//        pager.addOnPageChangeListener(new HomeOnPageChangeListener());
//
//        fragments = new ArrayList<>();
//        mTabLayout = (TabLayout) getContentView().findViewById(R.id.id_tablayout);
//        mViewPager = (ViewPager) getContentView().findViewById(R.id.id_viewpager);
//
//        initViewpager();
        manager = getChildFragmentManager();
        item1 = (RadioButton) findViewById(R.id.item1);
        item2 = (RadioButton) findViewById(R.id.item2);
        group = (RadioGroup) findViewById(R.id.group);
        textJokeFragment = new NewTextJokeFragment();
        switchContent(textJokeFragment);
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId==R.id.item1){
                    if (textJokeFragment==null){
                        textJokeFragment = new NewTextJokeFragment();
                    }
                    switchContent(textJokeFragment);
                    item1.setTextColor(ContextCompat.getColor(getActivity(),R.color.colorAccent));
                    item2.setTextColor(ContextCompat.getColor(getActivity(),R.color.white));
                }else if (checkedId==R.id.item2){
                    if (imageJokeFragment==null){
                        imageJokeFragment = new NewImageJokeFragment();
                    }
                    switchContent(imageJokeFragment);
                    item1.setTextColor(ContextCompat.getColor(getActivity(),R.color.white));
                    item2.setTextColor(ContextCompat.getColor(getActivity(),R.color.colorAccent));
                }
            }
        });

    }
    private Fragment current;

    /**
     * 切换当前显示的fragment
     */
    public void switchContent(Fragment to) {
        if (current != to) {
            FragmentTransaction transaction = manager.beginTransaction();

            if (current != null) {
                transaction.hide(current);
            }
            if (!to.isAdded()) { // 先判断是否被add过
                transaction.add(R.id.f_content, to).commit();
            } else {

                transaction.show(to).commit(); // 隐藏当前的fragment，显示下一个
            }
            current = to;
        }
    }

    private void initViewpager() {
        titles.add("选项1");
        titles.add("选项2");
        fragments.add(new NewTextJokeFragment());
        fragments.add(new NewImageJokeFragment());
        // 初始化ViewPager的适配器，并设置给它
        mViewPagerAdapter = new MyViewPagerAdapter(getChildFragmentManager(), titles, fragments);
        mViewPager.setAdapter(mViewPagerAdapter);
        // 设置ViewPager最大缓存的页面个数
        mViewPager.setOffscreenPageLimit(0);
        // 给ViewPager添加页面动态监听器（为了让Toolbar中的Title可以变化相应的Tab的标题）
        mViewPager.addOnPageChangeListener(new HomeOnPageChangeListener());

        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        // 将TabLayout和ViewPager进行关联，让两者联动起来
        mTabLayout.setupWithViewPager(mViewPager);
        // 设置Tablayout的Tab显示ViewPager的适配器中的getPageTitle函数获取到的标题
        mTabLayout.setTabsFromPagerAdapter(mViewPagerAdapter);
        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
    }


}

package com.wzh.fun.chat.activity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.wzh.fun.R;
import com.wzh.fun.chat.fragment.FollowFragment;
import com.wzh.fun.ui.activity.BaseActivity;

/**
 * Created by WZH on 2016/12/11.
 */

public class FollowActivity extends BaseActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow);
        int type = getIntent().getIntExtra(FollowFragment.TYPE, FollowFragment.TYPE_FOLLOWER);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        FollowFragment fragment = new FollowFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(FollowFragment.TYPE,type);
        fragment.setArguments(bundle);
        //加到Activity中
        fragmentTransaction.add(R.id.follow_content,fragment);
        //记住提交
        fragmentTransaction.commit();
    }
}

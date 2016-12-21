package com.wzh.fun.chat.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.wzh.fun.R;
import com.wzh.fun.chat.fragment.FollowFragment;
import com.wzh.fun.chat.fragment.PersonFragment;
import com.wzh.fun.ui.activity.BaseActivity;

/**
 * Created by WZH on 2016/12/4.
 */

public class PersonInfoActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        PersonFragment fragment = new PersonFragment();
        //加到Activity中
        fragmentTransaction.add(R.id.circle,fragment);
        //记住提交
        fragmentTransaction.commit();
    }
}

package com.wzh.fun.chat.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.wzh.fun.R;
import com.wzh.fun.chat.fragment.CircleFragment;
import com.wzh.fun.ui.activity.BaseActivity;

/**
 * Created by zhihao.wen on 2016/11/25.
 */

public class MainChatActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_circle);
//        FragmentManager manager = getSupportFragmentManager();
//        FragmentTransaction transaction = manager.beginTransaction();
//        CircleFragment fragment = new CircleFragment();
//        transaction.add(R.id.circle, fragment);
//        transaction.commit();
    }

}

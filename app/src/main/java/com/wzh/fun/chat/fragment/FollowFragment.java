package com.wzh.fun.chat.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.wzh.fun.App;
import com.wzh.fun.R;
import com.wzh.fun.chat.CircleServerUtils;
import com.wzh.fun.chat.adapter.FollowAdapter;
import com.wzh.fun.chat.adapter.UserAdapter;
import com.wzh.fun.ui.fragment.BaseFragment;
import com.wzh.fun.view.listview.CustomPtrFrameLayout;
import com.wzh.fun.view.listview.LoadMoreListView;
import com.wzh.fun.view.listview.PtrDefaultHandler;
import com.wzh.fun.view.listview.PtrFrameLayout;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * Created by WZH on 2016/12/11.
 */

public class FollowFragment extends BaseFragment implements BaseFragment.OnReloadDataListener {
    private CustomPtrFrameLayout customPtrFrameLayout;
    private LoadMoreListView list;
    private List<AVUser> users;
    private Subscriber subscriber;
    private int skip = 0, limit = 15;
    private FollowAdapter adapter ;
    public static final int TYPE_FOLLOWER = 0;
    public static final int TYPE_FOLLOWING = 1;
    public static final String TYPE = "type";
    public static final String USER_ID = "userId";
    public int type;
    public AVUser user;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         super.onCreateView(inflater, container, savedInstanceState);
        setContentView(R.layout.fragment_follow);
        setTitle("与我相关");
        setBackView();
        initArgument();
        initView();
        return getContentView();
    }

    private void initArgument() {
            int type = getActivity().getIntent().getIntExtra(TYPE, TYPE_FOLLOWER);
            String userId = getActivity().getIntent().getStringExtra(USER_ID);
            this.type = type;
            this.user = AVUser.getCurrentUser();
            if (type == TYPE_FOLLOWER) {
                setTitle(getResources().getString(R.string.status_followers));
            } else {
                setTitle(getResources().getString(R.string.status_following));
            }
    }

    private void initView() {
        users = new ArrayList<>();
        adapter = new FollowAdapter(getActivity());
        list = (LoadMoreListView) findViewById(R.id.list);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PersonFragment.go(getActivity(), adapter.getDatas().get(position));
            }
        });
        customPtrFrameLayout = (CustomPtrFrameLayout) findViewById(R.id.contentView);
        customPtrFrameLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                list.setHasMore();
                skip = 0 ;
                request(false);
            }
        });
        request(true);
    }
    @Override
    public void request(boolean isRefresh) {
        if (isRefresh) {
            showLoadingPage("正在加载中...", R.drawable.ic_loading);
        }
        subscriber = new Subscriber<List<AVUser>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.e("xxx",e.toString());
                showErrorView("数据加载失败,点击重试",R.drawable.ic_error);
            }

            @Override
            public void onNext(List<AVUser> data) {
                showContentView();
                customPtrFrameLayout.refreshComplete();
                if (data == null || data.size() < 1) {
                    list.setNoMore();
                    return;
                }
                if (skip==0){
                    if (adapter!=null){
                        list.setHasMore();
                        adapter.clear();
                    }
                }
                adapter.addAll(data);
            }
        };
        try {
            if (type == TYPE_FOLLOWER) {
                CircleServerUtils.getFollowers(user,skip, limit, subscriber);
            } else {
                CircleServerUtils.getFollowings(user,skip, limit, subscriber);
            }
        } catch (AVException e) {
            e.printStackTrace();
        }catch (Exception e) {
            e.printStackTrace();
        }

    }
}

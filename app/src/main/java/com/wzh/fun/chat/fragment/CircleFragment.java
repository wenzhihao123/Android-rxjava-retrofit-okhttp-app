package com.wzh.fun.chat.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.avos.avoscloud.*;
import com.wzh.fun.R;
import com.wzh.fun.chat.CircleServerUtils;
import com.wzh.fun.chat.activity.FindUserActivity;
import com.wzh.fun.chat.activity.FollowActivity;
import com.wzh.fun.chat.activity.ImageLookActivity;
import com.wzh.fun.chat.activity.StatusSendActivity;
import com.wzh.fun.chat.adapter.StatusListAdapter;
import com.wzh.fun.chat.entity.Status;
import com.wzh.fun.event.LoginSuccessdEvent;
import com.wzh.fun.ui.fragment.BaseFragment;
import com.wzh.fun.view.listview.CustomPtrFrameLayout;
import com.wzh.fun.view.listview.LoadMoreListView;
import com.wzh.fun.view.listview.PtrDefaultHandler;
import com.wzh.fun.view.listview.PtrFrameLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import rx.Observable;
import rx.Subscriber;

import static android.app.Activity.RESULT_OK;

/**
 * Created by zhihao.wen on 2016/11/29.
 */

public class CircleFragment extends BaseFragment implements View.OnClickListener {
    private StatusListAdapter adapter;
    private LoadMoreListView list;
    private CustomPtrFrameLayout customPtrFrameLayout;
    private Observable observable;
    private Subscriber subscriber;
    private long maxId = 0;
    private int limit = 15;
    int skip = 0;
    private View header ;
    private TextView send ,fans,focus,find;
    private static final int SEND_REQUEST = 2;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        setContentView(R.layout.fragment_main_circle);
        EventBus.getDefault().register(this);
        setTitle("段子圈");
        initView();
        return getContentView();
    }

    private void initView() {
        if (AVUser.getCurrentUser()==null){
            showLoginView();
            return;
        }
        header = LayoutInflater.from(getActivity()).inflate(R.layout.layout_circlr_header,null,false);
        send = (TextView) header.findViewById(R.id.send);
        send.setOnClickListener(this);
        focus = (TextView) header.findViewById(R.id.focus);
        focus.setOnClickListener(this);
        fans = (TextView) header.findViewById(R.id.fans);
        fans.setOnClickListener(this);
        find = (TextView) header.findViewById(R.id.find);
        find.setOnClickListener(this);
        adapter = new StatusListAdapter(getActivity());
        list = (LoadMoreListView) findViewById(R.id.list);
        if (list.getHeaderViewsCount()==0){
            list.addHeaderView(header);
        }
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i>=1){
                    Intent intent = new Intent(getActivity(), ImageLookActivity.class);
                    intent.putExtra("url",adapter.getDatas().get(i-1).getInnerStatus().getImageUrl());
                    startActivity(intent);
                }

            }
        });
        customPtrFrameLayout = (CustomPtrFrameLayout) findViewById(R.id.contentView);
        customPtrFrameLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                skip = 0;
                list.setHasMore();
                request(false);
            }
        });
        list.setOnGetMoreListener(new LoadMoreListView.OnGetMoreListener() {
            @Override
            public void onGetMore() {
                skip += limit;
                request(false);
            }
        });
        request(true);
    }
    @Subscribe
    public  void onEventMainThread(LoginSuccessdEvent event){
        initView();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);//取消注册
    }
    private void request(final boolean isRefresh) {
        if (isRefresh) {
            showLoadingPage("正在加载中...", R.drawable.ic_loading);
        }
        subscriber = new Subscriber<List<Status>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                list.setNoMore();
            }

            @Override
            public void onNext(List<Status> data) {
                list.getMoreComplete();
                customPtrFrameLayout.refreshComplete();
                showContentView();
                if (data == null || data.size() < 1) {
                    list.setNoMore();
                    return;
                }else {
                    if (skip==0){
                        adapter.clear();
                    }
                    adapter.addAll(data);
                }

            }
        };
        Log.e("xxxxxxxxx", "maxid: "+maxId+"---skip="+skip);
        try {
            maxId = getMaxId(skip, adapter.getDatas());
            if (maxId == 0) {
                list.setNoMore();
                list.getMoreComplete();
            } else {
                CircleServerUtils.getStatusDatas(maxId, limit, subscriber);
            }
        } catch (AVException e) {
            e.printStackTrace();
        }
    }

    public static long getMaxId(int skip, List<Status> currentDatas) {
        long maxId;
        if (skip == 0) {
            maxId = Long.MAX_VALUE;
        } else {
            AVStatus lastStatus = currentDatas.get(currentDatas.size() - 1).getInnerStatus();
            maxId = lastStatus.getMessageId() - 1;
        }
        return maxId;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == SEND_REQUEST) {
               request(false);
            }
        }
    }
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.send) {
            Intent intent = new Intent(getActivity(), StatusSendActivity.class);
            startActivityForResult(intent, SEND_REQUEST);
        }else if (v.getId() == R.id.find){
            startActivity(new Intent(getActivity(),FindUserActivity.class));
        }else if (v.getId() == R.id.fans){
            Intent intent = new Intent(getActivity(),FollowActivity.class) ;
            intent.putExtra(FollowFragment.TYPE,FollowFragment.TYPE_FOLLOWER);
            startActivity(intent);
        }else if (v.getId() == R.id.focus){
            Intent intent = new Intent(getActivity(),FollowActivity.class) ;
            intent.putExtra(FollowFragment.TYPE,FollowFragment.TYPE_FOLLOWING);
            startActivity(intent);
        }
    }
}

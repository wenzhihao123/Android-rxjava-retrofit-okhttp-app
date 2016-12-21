package com.wzh.fun.chat.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FollowCallback;
import com.bumptech.glide.Glide;
import com.wzh.fun.R;
import com.wzh.fun.chat.CircleServerUtils;
import com.wzh.fun.chat.activity.PersonInfoActivity;
import com.wzh.fun.chat.adapter.StatusListAdapter;
import com.wzh.fun.chat.entity.Status;
import com.wzh.fun.ui.fragment.BaseFragment;
import com.wzh.fun.utils.StatusNetAsyncTask;
import com.wzh.fun.view.listview.CustomPtrFrameLayout;
import com.wzh.fun.view.listview.LoadMoreListView;
import com.wzh.fun.view.listview.PtrDefaultHandler;
import com.wzh.fun.view.listview.PtrFrameLayout;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

public class PersonFragment extends BaseFragment implements BaseFragment.OnReloadDataListener, View.OnClickListener {
    public static final String USER_ID = "userId";
    public static final int CANCEL_FOLLOW = 0;
    public static final int FOLLOW = 1;
    private ImageView avatar ;
    private TextView focus ;
    private TextView nameView ;
    private CustomPtrFrameLayout customPtrFrameLayout;
    private LoadMoreListView listView;
    private StatusListAdapter adapter;
    private List<Status> statuses;
    private int skip = 0, limit = 15;
    private TextView followActionBtn;
    private TextView followStatusView;
    private View followLayout;
    private int followStatus;
    private AVUser user;
    boolean myself;
    int actionType = -1;
    private Subscriber subscriber;
    private View header ;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        setContentView(R.layout.status_person_layout);
        setTitle("查看资料");
        setBackView();
        init();
        initList();
        return getContentView();
    }
    private void initList() {
        statuses = new ArrayList<>();
        adapter = new StatusListAdapter(getActivity());
        listView = (LoadMoreListView) findViewById(R.id.status_List);
        listView.setAdapter(adapter);
        listView.addHeaderView(header);
        listView.setOnGetMoreListener(new LoadMoreListView.OnGetMoreListener() {
            @Override
            public void onGetMore() {
                skip += limit;
                request(false);
            }
        });
        customPtrFrameLayout = (CustomPtrFrameLayout) findViewById(R.id.contentView);
        customPtrFrameLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                skip = 0;
                request(false);
            }
        });
        request(true);
        refresh();
    }

    private void init() {
        Intent intent = getActivity().getIntent();
        user = intent.getParcelableExtra(USER_ID);

        header = LayoutInflater.from(getActivity()).inflate(R.layout.user_info_header,null,false);
        avatar = (ImageView) header.findViewById(R.id.avatar);
        focus = (TextView) header.findViewById(R.id.focus);
        followActionBtn = (TextView) header.findViewById(R.id.followAction);
        followActionBtn.setOnClickListener(this);
        followStatusView = (TextView) header.findViewById(R.id.followStatus);
        nameView = (TextView) header.findViewById(R.id.nameView);
        followLayout = header.findViewById(R.id.followLayout);
        inflateUser(user);
    }
    private void refresh() {
        new StatusNetAsyncTask(getActivity()) {
            @Override
            protected void doInBack() throws Exception {
                if (!myself) {
                    followStatus = CircleServerUtils.followStatus(user);
                }
            }

            @Override
            protected void onPost(Exception e) {
                if (CircleServerUtils.filterException(getActivity(), e)) {
                    if (myself) {
                        followStatusView.setVisibility(View.GONE);
                        followLayout.setVisibility(View.GONE);
                        followActionBtn.setVisibility(View.GONE);
                        return;
                    }
                    followStatusView.setVisibility(View.VISIBLE);
                    followActionBtn.setVisibility(View.VISIBLE);

                    int followStatusDescId = R.string.status_none_follow_desc;
                    switch (followStatus) {
                        case CircleServerUtils.MUTUAL_FOLLOW:
                            followStatusDescId = R.string.status_mutual_follow;
                            break;
                        case CircleServerUtils.FOLLOWER:
                            followStatusDescId = R.string.status_follower_desc;
                            break;
                        case CircleServerUtils.FOLLOWING:
                            followStatusDescId = R.string.status_following_desc;
                            break;
                        case CircleServerUtils.NONE_FOLLOW:
                            followStatusDescId = R.string.status_none_follow_desc;
                            break;
                    }
                    String followStatusDesc = getString(followStatusDescId);
                    followStatusView.setText(followStatusDesc);

                    int followButtonResId;
                    if (followStatus == CircleServerUtils.MUTUAL_FOLLOW ||
                            followStatus == CircleServerUtils.FOLLOWING) {
                        actionType = CANCEL_FOLLOW;
                        followButtonResId = R.string.status_cancelFollow;
                    } else {
                        actionType = FOLLOW;
                        followButtonResId = R.string.status_follow;
                    }

                    followActionBtn.setText(getString(followButtonResId));
                }
            }
        }.execute();
    }
    private void inflateUser(AVUser user) {
        AVFile file = user.getAVFile("avatar");
        if (file != null) {
            Glide.with(getActivity()).load(file.getUrl()).placeholder(R.drawable.default_head).into(avatar);
        }else {
            Glide.with(getActivity()).load(R.drawable.default_head).into(avatar);
        }
        nameView.setText(user.getUsername());
    }

    public static void go(Context context, AVUser item) {
        Intent intent = new Intent(context, PersonInfoActivity.class);
        intent.putExtra(PersonFragment.USER_ID, item);
        context.startActivity(intent);
    }

    @Override
    public void request(boolean isRefresh) {
        if (isRefresh) {
            showLoadingPage("正在加载中...", R.drawable.ic_loading);
        }
        subscriber = new Subscriber<List<Status>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                showErrorView("数据加载失败,点击重试", R.drawable.ic_error);
            }

            @Override
            public void onNext(List<Status> data) {
                showContentView();
                listView.getMoreComplete();
                customPtrFrameLayout.refreshComplete();
                if (data == null || data.size() < 1) {
                    listView.setNoMore();
                    return;
                }
                if (skip == 0) {
                    listView.setHasMore();
                    if (adapter != null) {
                        adapter.clear();
                    }
                }
                adapter.addAll(data);
            }
        };
        try {
            CircleServerUtils.getUserStatusList(user,skip, limit, subscriber);
        } catch (AVException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View v) {
        if (v.getId()== followActionBtn.getId()){
            if (actionType != -1) {
                if (myself) {
                    return;
                }
                boolean follow;
                if (actionType == FOLLOW) {
                    follow = true;
                } else {
                    follow = false;
                }
                CircleServerUtils.followAction(user, follow, new FollowCallback() {

                    @Override
                    public void done(AVObject object, AVException e) {
                        if (CircleServerUtils.filterException(getActivity(), e)) {
                            refresh();
                        }
                    }
                });
            }
        }
    }
}

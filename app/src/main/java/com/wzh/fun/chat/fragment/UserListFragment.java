package com.wzh.fun.chat.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.GridView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.wzh.fun.R;
import com.wzh.fun.chat.CircleServerUtils;
import com.wzh.fun.chat.adapter.UserAdapter;
import com.wzh.fun.ui.fragment.BaseFragment;
import com.wzh.fun.view.AutoLoadListener;
import com.wzh.fun.view.listview.CustomPtrFrameLayout;
import com.wzh.fun.view.listview.LoadMoreListView;
import com.wzh.fun.view.listview.PtrDefaultHandler;
import com.wzh.fun.view.listview.PtrFrameLayout;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * Created by WZH on 2016/12/3.
 */

public class UserListFragment extends BaseFragment implements BaseFragment.OnReloadDataListener {
    private CustomPtrFrameLayout customPtrFrameLayout;
    private GridView gridView;
    private List<AVUser> users;
    private Subscriber subscriber;
    private int skip = 0, limit = 15;
    private UserAdapter adapter ;
    private int mImageThumbSize;
    private int mImageThumbSpacing;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        setContentView(R.layout.fragment_user_list);
        setTitle("发现段子手");
        setBackView();
        initView();
        return getContentView();
    }

    private void initView() {
        mImageThumbSize = getResources().getDimensionPixelSize(
                R.dimen.image_thumbnail_size);
        mImageThumbSpacing = getResources().getDimensionPixelSize(
                R.dimen.image_thumbnail_spacing);
        users = new ArrayList<>();
        adapter = new UserAdapter(getActivity());
        gridView = (GridView) findViewById(R.id.list);
        gridView.setAdapter(adapter);
        gridView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        final int numColumns = (int) Math.floor(gridView
                                .getWidth()
                                / (mImageThumbSize + mImageThumbSpacing));
                        if (numColumns > 0) {
                            int columnWidth = (gridView.getWidth() / numColumns)
                                    - mImageThumbSpacing;
                            adapter.setItemHeight(columnWidth);
                            gridView.getViewTreeObserver()
                                    .removeGlobalOnLayoutListener(this);
                        }
                    }
                });
        AutoLoadListener autoLoadListener = new AutoLoadListener(callBack);
        gridView.setOnScrollListener(autoLoadListener);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PersonFragment.go(getActivity(), adapter.getDatas().get(position));
            }
        });
        customPtrFrameLayout = (CustomPtrFrameLayout) findViewById(R.id.contentView);
        customPtrFrameLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                skip = 0 ;
                request(false);
            }
        });
        request(true);
    }
    AutoLoadListener.AutoLoadCallBack callBack = new AutoLoadListener.AutoLoadCallBack() {
        public void execute() {
                skip +=limit;
                request(false);
        }
    };

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
                showErrorView("数据加载失败,点击重试",R.drawable.ic_error);
            }

            @Override
            public void onNext(List<AVUser> data) {
                showContentView();
                customPtrFrameLayout.refreshComplete();
                if (data == null || data.size() < 1) {
                    return;
                }
                if (skip==0){
                    if (adapter!=null){
                        adapter.clear();
                    }
                }
                adapter.addAll(data);
            }
        };
        try {
            CircleServerUtils.findUsers(skip, limit, subscriber);
        } catch (AVException e) {
            e.printStackTrace();
        }

    }
}

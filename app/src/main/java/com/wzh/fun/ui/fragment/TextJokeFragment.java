package com.wzh.fun.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.wzh.fun.R;
import com.wzh.fun.adapter.TextJokeAdapter;
import com.wzh.fun.entity.TextJokeEntity;
import com.wzh.fun.http.ApiResponseWraper;
import com.wzh.fun.http.NetWorkUtil;
import com.wzh.fun.http.RequestParam;
import com.wzh.fun.utils.Constant;
import com.wzh.fun.view.listview.CustomPtrFrameLayout;
import com.wzh.fun.view.listview.LoadMoreListView;
import com.wzh.fun.view.listview.PtrDefaultHandler;
import com.wzh.fun.view.listview.PtrFrameLayout;

import java.util.ArrayList;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by zhihao.wen on 2016/11/3.
 */

public class TextJokeFragment extends BaseFragment implements BaseFragment.OnReloadDataListener{
    private CustomPtrFrameLayout customPtrFrameLayout ;
    private LoadMoreListView listView ;
    private Subscription sb ;
    private TextJokeAdapter adapter ;
    private ArrayList<TextJokeEntity> textJokeEntities ;
    private Observer<ApiResponseWraper<TextJokeEntity>> obXW ;
    private int currentPage = 1 ;
    private int pagetSize = 10 ;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        setContentView(R.layout.fragment_text);
        setTitle("段子大全");
        initView();
        initData();
        return getContentView() ;
    }

    private void initData() {
        obXW = new Observer<ApiResponseWraper<TextJokeEntity>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                showErrorView("数据加载失败,点击重试",R.drawable.ic_error);
            }

            @Override
            public void onNext(ApiResponseWraper<TextJokeEntity> xwEntity) {
                if (currentPage==1){
                    textJokeEntities.clear();
                }
                textJokeEntities.addAll(xwEntity.getResult().getData());
                adapter.notifyDataSetChanged();
                customPtrFrameLayout.refreshComplete();
                listView.getMoreComplete();
                showContentView();
                if (xwEntity.getResult().getData().size()<pagetSize){
                    listView.setNoMore();
                }
                Log.e("main","==="+xwEntity.getResult().getData().toString());
            }
        };
        request(true);
    }

    private void initView() {
        listView = (LoadMoreListView) findViewById(R.id.list);
        listView.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                listView.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        customPtrFrameLayout = (CustomPtrFrameLayout) findViewById(R.id.contentView);
        customPtrFrameLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                currentPage=1 ;
                request(false);
            }
        });
        textJokeEntities = new ArrayList<>();
        listView.setOnGetMoreListener(new LoadMoreListView.OnGetMoreListener() {
            @Override
            public void onGetMore() {
                currentPage++;
                request(false);
            }
        });
        adapter = new TextJokeAdapter(getActivity(),textJokeEntities);
        listView.setAdapter(adapter);
        setOnReloadDataListener(this);
    }

    @Override
    public void request(boolean isRefresh) {
        if (isRefresh){
            showLoadingPage("正在加载中...",R.drawable.ic_loading);
        }
        RequestParam param = new RequestParam();
        param.put("key", Constant.KEY);
        param.put("time", System.currentTimeMillis()/1000);
        param.put("sort", "desc");
        param.put("pagesize",pagetSize);
        param.put("page",currentPage);
        sb = NetWorkUtil.getTimeTextJokeApi()
                .getTextJoke(param)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(obXW);
    }
}

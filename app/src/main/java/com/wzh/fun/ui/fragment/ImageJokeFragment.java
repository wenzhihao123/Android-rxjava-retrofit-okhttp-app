package com.wzh.fun.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.wzh.fun.R;
import com.wzh.fun.adapter.ImageJokeAdapter;
import com.wzh.fun.adapter.TextJokeAdapter;
import com.wzh.fun.chat.activity.ImageLookActivity;
import com.wzh.fun.entity.ImageJokeEntity;
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

public class ImageJokeFragment extends BaseFragment implements BaseFragment.OnReloadDataListener{
    private CustomPtrFrameLayout customPtrFrameLayout ;
    private LoadMoreListView listView ;
    private Subscription sb ;
    private ImageJokeAdapter adapter ;
    private ArrayList<ImageJokeEntity> imageJokeEntities ;
    private Observer<ApiResponseWraper<ImageJokeEntity>> obXW ;
    private int currentPage = 1 ;
    private int pagetSize =10 ;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        setContentView(R.layout.fragment_image);
        setTitle("内涵趣图");
        initView();
        initData();
        return getContentView() ;
    }

    private void initData() {
        obXW = new Observer<ApiResponseWraper<ImageJokeEntity>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                showErrorView("数据加载失败,点击重试",R.drawable.ic_error);
            }

            @Override
            public void onNext(ApiResponseWraper<ImageJokeEntity> xwEntity) {
                if (currentPage==1){
                    imageJokeEntities.clear();
                }
                imageJokeEntities.addAll(xwEntity.getResult().getData());
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
        customPtrFrameLayout = (CustomPtrFrameLayout) findViewById(R.id.contentView);
        customPtrFrameLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                currentPage=1 ;
                request(false);
            }
        });
        imageJokeEntities = new ArrayList<>();
        listView.setOnGetMoreListener(new LoadMoreListView.OnGetMoreListener() {
            @Override
            public void onGetMore() {
                currentPage++;
                request(false);
            }
        });
        adapter = new ImageJokeAdapter(getActivity(),imageJokeEntities);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), ImageLookActivity.class);
                intent.putExtra("url",adapter.getItem(i).getUrl());
                startActivity(intent);
            }
        });
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
        sb = NetWorkUtil.getTimeImageJokeApi()
                .getImageJoke(param)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(obXW);
    }
}

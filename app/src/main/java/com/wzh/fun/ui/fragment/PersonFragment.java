package com.wzh.fun.ui.fragment;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.GetCallback;
import com.bumptech.glide.Glide;
import com.wzh.fun.App;
import com.wzh.fun.R;
import com.wzh.fun.chat.CircleServerUtils;
import com.wzh.fun.chat.activity.LoginActivity;
import com.wzh.fun.chat.activity.SuggestActivity;
import com.wzh.fun.entity.TextJokeEntity;
import com.wzh.fun.entity.WeatherEntity;
import com.wzh.fun.event.LoginSuccessdEvent;
import com.wzh.fun.http.ApiResponseWraperNoData;
import com.wzh.fun.http.ApiResponseWraperNoList;
import com.wzh.fun.http.NetWorkUtil;
import com.wzh.fun.http.RequestParam;
import com.wzh.fun.utils.Constant;
import com.wzh.fun.utils.DataCleanManager;
import com.wzh.fun.utils.GlideCircleTransform;
import com.wzh.fun.view.ShowDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by WZH on 2016/11/4.
 */

public class PersonFragment extends BaseFragment implements BaseFragment.OnReloadDataListener, View.OnClickListener {
    private Subscription sb ;
    private Observer<ApiResponseWraperNoList<WeatherEntity>> obXW ;
    private ShowDialog dialog ;
    private View loginOut ;
    private TextView nameView;
    private ImageView avatar ;
    private TextView cacheSizeTv ;
    private String cacheSize ;
    private TextView versionCode ;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         super.onCreateView(inflater, container, savedInstanceState);
         setContentView(R.layout.fragment_person);
         EventBus.getDefault().register(this);
         initView();
         return getContentView();
    }

    private void initView() {
        dialog = new ShowDialog();
        loginOut =  findViewById(R.id.login_out) ;
        loginOut.setOnClickListener(this);

        versionCode = (TextView) findViewById(R.id.versionCode);
        versionCode.setText("V "+getVersion());
        cacheSizeTv = (TextView) findViewById(R.id.cacheSize);
        refreshCache();
        findViewById(R.id.clear_cache).setOnClickListener(this);
        findViewById(R.id.suggest).setOnClickListener(this);
        nameView = (TextView) findViewById(R.id.nameView);
        avatar = (ImageView) findViewById(R.id.avatar);
        avatar.setOnClickListener(this);

        checkUser();
    }

    private void initData() {
        if (AVUser.getCurrentUser()==null){
            showLoginView();
            return;
        }
        obXW = new Observer<ApiResponseWraperNoList<WeatherEntity>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                showErrorView("数据加载失败,点击重试",R.drawable.ic_error);
            }

            @Override
            public void onNext(ApiResponseWraperNoList<WeatherEntity> xwEntity) {
                showContentView();
                Log.e("main","==="+xwEntity.getResult().toString());
            }
        };
        request(true);
    }

    @Override
    public void request(boolean isRefresh) {
        if (isRefresh){
            showLoadingPage("正在加载中...",R.drawable.ic_loading);
        }
        RequestParam param = new RequestParam();
        param.put("key", Constant.WKEY);
        param.put("cityname","北京");
        sb = NetWorkUtil.getWeatherApi()
                .getWeather(param)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(obXW);
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.login_out){
            dialog.show(getActivity(), "", "是否确认注销？", new ShowDialog.OnBottomClickListener() {
                @Override
                public void positive() {
                    AVUser.getCurrentUser().logOut();
                    EventBus.getDefault().post(new LoginSuccessdEvent());
                }
                @Override
                public void negtive() {

                }
            });
        }else if (v.getId()==avatar.getId()){
            if (AVUser.getCurrentUser()==null){
                startActivity(new Intent(getActivity(), LoginActivity.class));
            }else {
                com.wzh.fun.chat.fragment.PersonFragment.go(getActivity(), AVUser.getCurrentUser());
            }
        }else if (v.getId()==R.id.clear_cache){
            dialog.show(getActivity(), "", "是否清除缓存？", new ShowDialog.OnBottomClickListener() {
                @Override
                public void positive() {
                    DataCleanManager.cleanApplicationData(getActivity());
                    refreshCache();
                }
                @Override
                public void negtive() {

                }
            });

        }else if (v.getId()==R.id.suggest){
            startActivity(new Intent(getActivity(), SuggestActivity.class));
        }
    }
    @Subscribe
    public  void onEventMainThread(LoginSuccessdEvent event){
        checkUser();
    }
    public void checkUser(){
        AVUser user = AVUser.getCurrentUser() ;
        if(user==null){
            loginOut.setVisibility(View.GONE);
            nameView.setText("请登录");
            avatar.setImageResource(R.drawable.default_head);
            return;
        }
        user.fetchInBackground(new GetCallback<AVObject>() {
            @Override
            public void done(AVObject avObject, AVException e) {
                AVUser u = (AVUser) avObject;
                if (u==null){
                    loginOut.setVisibility(View.GONE);
                    nameView.setText("请登录");
                    avatar.setImageResource(R.drawable.default_head);
                }else {
                    nameView.setText(u.getUsername());
                    loginOut.setVisibility(View.VISIBLE);
                    if (u.getAVFile("avatar")==null){
                        avatar.setImageResource(R.drawable.default_head);
                    }else {
                        String path  = u.getAVFile("avatar").getUrl();
                        if (!TextUtils.isEmpty(path)) {
                            Glide.with(getActivity()).load(path).placeholder(R.drawable.default_head).into(avatar);
                        }else {
                            avatar.setImageResource(R.drawable.default_head);
                        }
                    }

                }

            }
        });

    }

    public void refreshCache(){
        try {
            cacheSize=  DataCleanManager.getTotalCacheSize(getActivity());
            cacheSizeTv.setText(cacheSize);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 获取版本号
     *
     * @return 当前应用的版本号
     */
    public String getVersion() {
        try {
            PackageManager manager = getActivity().getPackageManager();
            PackageInfo info = manager.getPackageInfo(getActivity().getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden){
            refreshCache();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}

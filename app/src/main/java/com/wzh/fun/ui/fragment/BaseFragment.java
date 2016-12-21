package com.wzh.fun.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.wzh.fun.R;
import com.wzh.fun.chat.activity.LoginActivity;

/**
 * Created by zhihao.wen on 2016/4/7.
 */
public abstract  class BaseFragment<T> extends Fragment {
    private View view ;
    private LayoutInflater inflater;
    private  ViewGroup container ;

    private View errorView ,contentView;
    private TextView error_tv ;
    private ImageView error_iv ;
    private RotateAnimation animation ;

    private View loginView ;
    private TextView loginTv ;

    public View setContentView(int resourceId) {
        view = inflater.inflate(resourceId, container, false);
        return view;
    }
    public View getContentView(){
        return  this.view ;
    }
    private void init(){
        errorView = findViewById(R.id.errorView);
        if (errorView!=null){
            errorView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onReloadDataListener!=null){
                        onReloadDataListener.request(true);
                    }
                }
            });
        }
        error_iv = (ImageView) findViewById(R.id.error_iv);
        error_tv = (TextView) findViewById(R.id.error_tv);
        contentView = findViewById(R.id.contentView);
        loginView = findViewById(R.id.loginView);
        loginTv = (TextView) findViewById(R.id.login_tv);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.inflater = inflater ;
        this.container = container ;
        return super.onCreateView(inflater,container,savedInstanceState);
    }
    /**
     * 设置显示右侧返回按钮
     */
    public void setBackView(){
        View backView = view.findViewById(R.id.back_view);
        if (backView==null){
            return;
        }
        backView.setVisibility(View.VISIBLE);
        backView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
    }

    /**
     * 设置显示标题
     * @param txt
     */
    public void setTitle(String txt){
        TextView title = (TextView) view.findViewById(R.id.title);
        if (title==null){
            return;
        }
        title.setVisibility(View.VISIBLE);
        title.setText(txt);
    }

    /**
     * 只显示右侧文字以及点击事件
     * @param txt
     * @param onClickListener
     */
    public void setRightText(String txt, View.OnClickListener onClickListener){
        TextView toolbar_righ_tv = (TextView) view.findViewById(R.id.toolbar_righ_tv);
        if (toolbar_righ_tv==null){
            return;
        }
        ImageView toolbar_righ_iv = (ImageView) view.findViewById(R.id.toolbar_righ_iv);
        if (toolbar_righ_iv==null){
            return;
        }
        toolbar_righ_iv.setVisibility(View.GONE);
        toolbar_righ_tv.setVisibility(View.VISIBLE);
        toolbar_righ_tv.setText(txt);
        toolbar_righ_tv.setOnClickListener(onClickListener);
    }

    /**
     * 右侧只显示一个图片
     * @param resId
     * @param onClickListener
     */
    public void setRightImage(int resId, View.OnClickListener onClickListener){
        TextView toolbar_righ_tv = (TextView) view.findViewById(R.id.toolbar_righ_tv);
        if (toolbar_righ_tv==null){
            return;
        }
        toolbar_righ_tv.setVisibility(View.GONE);
        ImageView toolbar_righ_iv = (ImageView)view.findViewById(R.id.toolbar_righ_iv);
        if (toolbar_righ_iv==null){
            return;
        }
        toolbar_righ_iv.setVisibility(View.VISIBLE);
        toolbar_righ_iv.setImageResource(resId);
        toolbar_righ_iv.setOnClickListener(onClickListener);
    }

    /**
     * 显示文字和图片，可以设置文字内容及字体颜色，图片资源
     * @param txt
     * @param txtColor
     * @param resId
     * @param onClickListener
     */
    public void setRightTextAndImage(String txt,int txtColor,int resId, View.OnClickListener onClickListener){
        TextView toolbar_righ_tv = (TextView) view.findViewById(R.id.toolbar_righ_tv);
        if (toolbar_righ_tv==null){
            return;
        }
        toolbar_righ_tv.setVisibility(View.VISIBLE);
        toolbar_righ_tv.setTextColor(txtColor);

        ImageView toolbar_righ_iv = (ImageView) view.findViewById(R.id.toolbar_righ_iv);
        if (toolbar_righ_iv==null){
            return;
        }
        toolbar_righ_iv.setVisibility(View.VISIBLE);
        toolbar_righ_iv.setImageResource(resId);

        toolbar_righ_iv.setOnClickListener(onClickListener);
        toolbar_righ_tv.setOnClickListener(onClickListener);
    }

    /**
     * 显示加载页面
     * @param tip
     * @param resId
     */
    public void showLoadingPage(String tip,int resId){
        init();
        if (errorView==null){
            return;
        }
        if (error_iv==null){
            return;
        }
        if (error_tv==null){
            return;
        }
        if (contentView==null){
            return;
        }
        if (contentView==null){
            return;
        }
        errorView.setVisibility(View.VISIBLE);
        contentView.setVisibility(View.GONE);
        if (loginView!=null){
            loginView.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(tip)){
            error_tv.setText(tip);
        }else {
            error_tv.setText("数据正在加载...");
        }
        error_iv.setImageResource(resId);
        /** 设置旋转动画 */
        if (animation==null){
            animation =new RotateAnimation(0f,359f, Animation.RELATIVE_TO_SELF,
                    0.5f,Animation.RELATIVE_TO_SELF,0.5f);
            animation.setDuration(1000);//设置动画持续时间
            /** 常用方法 */
            animation.setRepeatCount(Integer.MAX_VALUE);//设置重复次数
            animation.startNow();
        }
        error_iv.setAnimation(animation);

    }
    /**
     * 显示错误页面
     * @param message
     * @param resId
     */
    public void showErrorView(String message,int resId){
        init();
        if (errorView==null){
            return;
        }
        if (error_iv==null){
            return;
        }
        if (error_tv==null){
            return;
        }
        if (contentView==null){
            return;
        }
        if (contentView==null){
            return;
        }
        if (loginView!=null){
            loginView.setVisibility(View.GONE);
        }
        error_iv.setImageResource(resId);
        if (!TextUtils.isEmpty(message)){
            error_tv.setText(message);
        }else {
            error_tv.setText("数据加载失败！");
        }
        error_iv.setAnimation(null);
        errorView.setVisibility(View.VISIBLE);
        contentView.setVisibility(View.GONE);
    }
    /**
     * 显示登录页面
     */
    public void showLoginView(){
        init();
        if (loginView==null){
            return;
        }
        if (loginTv==null){
            return;
        }

        if (errorView!=null){
            errorView.setVisibility(View.GONE);
        }
        if (contentView!=null){
            contentView.setVisibility(View.GONE);
        }
        loginView.setVisibility(View.VISIBLE);
        loginTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),LoginActivity.class));
            }
        });
    }

    /**
     * 显示内容区域
     */
    public void showContentView(){
        init();
        if (errorView==null){
            return;
        }
        if (error_iv==null){
            return;
        }
        if (error_tv==null){
            return;
        }
        if (contentView==null){
            return;
        }
        if (contentView==null){
            return;
        }
        if (loginView!=null){
            loginView.setVisibility(View.GONE);
        }
        contentView.setVisibility(View.VISIBLE);
        errorView.setVisibility(View.GONE);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * 获取控件id
     * @param id
     * @return
     */
    public View findViewById(int id){
        return view.findViewById(id) ;
    }

    private OnReloadDataListener onReloadDataListener;

    public void setOnReloadDataListener(OnReloadDataListener onReloadDataListener) {
        this.onReloadDataListener = onReloadDataListener;
    }

    public interface OnReloadDataListener{
        void request(boolean isRefresh);
    }
}

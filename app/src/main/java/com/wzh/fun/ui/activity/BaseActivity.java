package com.wzh.fun.ui.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.wzh.fun.R;
import com.wzh.fun.ui.fragment.BaseFragment;


/**
 * Created by zhihao.wen on 2016/4/19.
 */
public class BaseActivity extends FragmentActivity {
    private View errorView ,contentView;
    private TextView error_tv ;
    private ImageView error_iv ;
    private RotateAnimation animation ;
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    private void init(){
        errorView = findViewById(R.id.errorView);
        errorView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onReloadDataListener!=null){
                    onReloadDataListener.request(true);
                }
            }
        });
        error_iv = (ImageView)findViewById(R.id.error_iv);
        error_tv = (TextView) findViewById(R.id.error_tv);
        contentView = findViewById(R.id.contentView);
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
        error_iv.setImageResource(resId);
        if (!TextUtils.isEmpty(message)){
            error_tv.setText(message);
        }
        error_iv.setAnimation(null);
        errorView.setVisibility(View.VISIBLE);
        contentView.setVisibility(View.GONE);
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
        contentView.setVisibility(View.VISIBLE);
        errorView.setVisibility(View.GONE);
    }

    /**
     * 设置显示右侧返回按钮
     */
    public void setBackView(){
        View backView = findViewById(R.id.back_view);
        if (backView==null){
            return;
        }
        backView.setVisibility(View.VISIBLE);
        backView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    /**
     * 设置显示标题
     * @param txt
     */
    public void setTitle(String txt){
        TextView title = (TextView) findViewById(R.id.title);
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
        TextView toolbar_righ_tv = (TextView) findViewById(R.id.toolbar_righ_tv);
        if (toolbar_righ_tv==null){
            return;
        }
        ImageView toolbar_righ_iv = (ImageView) findViewById(R.id.toolbar_righ_iv);
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
        TextView toolbar_righ_tv = (TextView) findViewById(R.id.toolbar_righ_tv);
        if (toolbar_righ_tv==null){
            return;
        }
        toolbar_righ_tv.setVisibility(View.GONE);
        ImageView toolbar_righ_iv = (ImageView) findViewById(R.id.toolbar_righ_iv);
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
        TextView toolbar_righ_tv = (TextView) findViewById(R.id.toolbar_righ_tv);
        if (toolbar_righ_tv==null){
            return;
        }
        toolbar_righ_tv.setVisibility(View.VISIBLE);
        toolbar_righ_tv.setTextColor(txtColor);

        ImageView toolbar_righ_iv = (ImageView) findViewById(R.id.toolbar_righ_iv);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    private BaseFragment.OnReloadDataListener onReloadDataListener;

    public void setOnReloadDataListener(BaseFragment.OnReloadDataListener onReloadDataListener) {
        this.onReloadDataListener = onReloadDataListener;
    }

    public interface OnReloadDataListener{
        void request(boolean isRefresh);
    }
}

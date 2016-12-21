package com.wzh.fun.chat.activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.avos.avoscloud.SignUpCallback;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.wzh.fun.R;
import com.wzh.fun.event.LoginSuccessdEvent;
import com.wzh.fun.ui.activity.BaseActivity;
import com.wzh.fun.ui.activity.MainActivity;
import com.wzh.fun.view.LoadingDialog;

import org.greenrobot.eventbus.EventBus;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import me.iwf.photopicker.PhotoPickerActivity;
import me.iwf.photopicker.utils.PhotoPickerIntent;

/**
 * Created by zhihao.wen on 2016/11/25.
 */

public class LoginActivity extends BaseActivity {
    private static final String TAG = "LoginActivity";
    private EditText username ;
    private EditText password ;
    private LoadingDialog dialog ;
    private View viewPart ;
    private ImageView  icon ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("用户登录");
        setBackView();
        initView();
    }
    public void click(View v){
        switch (v.getId()){
            case R.id.login:
                login();
                break ;
            case R.id.register:
                startActivity(new Intent(LoginActivity.this,RegestActivity.class));
                finish();
                break ;
        }
    }



    private void login() {
        String name = username.getText().toString();
        String pass = password.getText().toString();
        if(TextUtils.isEmpty(name)){
            Toast.makeText(this,"用户名不能为空！",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(pass)){
            Toast.makeText(this,"密码不能为空！",Toast.LENGTH_SHORT).show();
            return;
        }
        dialog.show();
        dialog.setMessage("正在登录");
        AVUser.logInInBackground(name, pass, new LogInCallback<AVUser>() {
            @Override
            public void done(AVUser avUser, AVException e) {
                if (dialog!=null){
                    dialog.dismiss();
                }
                if (e == null) {
                    Log.e(TAG, "done: 登录成功");
                    EventBus.getDefault().post(new LoginSuccessdEvent());
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this,"登录失败,请重试！",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }


    private void initView() {
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        viewPart = findViewById(R.id.view_part);
        icon = (ImageView) findViewById(R.id.icon);
        dialog = new LoadingDialog(this);
        dialog.setCanceledOnTouchOutside(false);
        password.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener(){
            //当键盘弹出隐藏的时候会 调用此方法。
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                //获取当前界面可视部分
                LoginActivity.this.getWindow().getDecorView().getWindowVisibleDisplayFrame(r);
                //获取屏幕的高度
                int screenHeight =  LoginActivity.this.getWindow().getDecorView().getRootView().getHeight();
                //此处就是用来获取键盘的高度的， 在键盘没有弹出的时候 此高度为0 键盘弹出的时候为一个正数
                int heightDifference = screenHeight - r.bottom;
                if (heightDifference>0){
                    viewPart.setTranslationY(-80);
                }else {
                    viewPart.setTranslationY(80);
                }
                Log.d("Keyboard Size", "Size: " + heightDifference);
            }

        });
    }

}

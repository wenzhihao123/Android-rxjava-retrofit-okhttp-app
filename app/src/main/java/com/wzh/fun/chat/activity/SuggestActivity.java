package com.wzh.fun.chat.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.SaveCallback;
import com.wzh.fun.R;
import com.wzh.fun.ui.activity.BaseActivity;

/**
 * author：Administrator on 2016/12/21 14:06
 * description:文件说明
 * version:版本
 */
public class SuggestActivity extends BaseActivity {
    private EditText tvEdit ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggest);
        setTitle("留下您的意见或建议");
        setBackView();
        tvEdit = (EditText) findViewById(R.id.suggest);
        setRightImage(R.drawable.icon_select, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                suggest();
            }
        });
    }

    private void suggest() {
        if (TextUtils.isEmpty(tvEdit.getText().toString())){
            Toast.makeText(SuggestActivity.this,"请填写内容！",Toast.LENGTH_SHORT).show();
            return;
        }
        AVObject todo = new AVObject("suggest");
        todo.put("content", tvEdit.getText().toString());
        todo.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    Toast.makeText(SuggestActivity.this,"反馈成功，谢谢！",Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(SuggestActivity.this,"反馈失败！",Toast.LENGTH_SHORT).show();
                    finish();
                    // 失败的话，请检查网络环境以及 SDK 配置是否正确
                }
            }
        });
    }
}

package com.wzh.fun.view;

/**
 * Created by WZH on 2016/12/12.
 */

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wzh.fun.R;

public class LoadingDialog extends Dialog {

    private TextView tv;

    public LoadingDialog(Context context) {
        super(context,R.style.loadingDialogStyle);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_loading);
        tv = (TextView)findViewById(R.id.tv);
        LinearLayout linearLayout = (LinearLayout)this.findViewById(R.id.LinearLayout);
        linearLayout.getBackground().setAlpha(210);
    }
    public void setMessage(String message){
        tv.setText(message);
    }
}


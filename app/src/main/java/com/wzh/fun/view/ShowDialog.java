package com.wzh.fun.view;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by WZH on 2016/12/11.
 */

public class ShowDialog {
    private CustomDialog selfDialog ;
    public ShowDialog() {
    }
    public void show(final Context ctx, String title, String message, final OnBottomClickListener onBottomClickListener){
        selfDialog = new CustomDialog(ctx);
        selfDialog.setTitle(title);
        selfDialog.setMessage(message);
        selfDialog.setYesOnclickListener("确定", new CustomDialog.onYesOnclickListener() {
            @Override
            public void onYesClick() {
                if (onBottomClickListener!=null){
                    onBottomClickListener.positive();
                }
                selfDialog.dismiss();
            }
        });
        selfDialog.setNoOnclickListener("取消", new CustomDialog.onNoOnclickListener() {
            @Override
            public void onNoClick() {
                if (onBottomClickListener!=null){
                    onBottomClickListener.negtive();
                }
                selfDialog.dismiss();
            }
        });
        selfDialog.show();
    }

    public interface OnBottomClickListener{
        void positive();
        void negtive();
    }
}

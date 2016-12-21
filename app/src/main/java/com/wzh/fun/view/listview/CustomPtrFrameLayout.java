package com.wzh.fun.view.listview;

import android.content.Context;
import android.util.AttributeSet;

import com.wzh.fun.view.listview.header.WindmillHeader;


/**
 * Created by zhihao.wen on 2016/4/21.
 */
public class CustomPtrFrameLayout extends PtrFrameLayout {
    public CustomPtrFrameLayout(Context context) {
        this(context,null);
    }

    public CustomPtrFrameLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CustomPtrFrameLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    private void initView() {
        final WindmillHeader header = new WindmillHeader(getContext());
        setHeaderView(header);
        addPtrUIHandler(header);
    }
}

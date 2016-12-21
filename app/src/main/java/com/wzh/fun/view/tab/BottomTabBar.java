package com.wzh.fun.view.tab;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.wzh.fun.R;

import java.util.List;

/**
 * Created by zhihao.wen on 2016/11/4.
 */

public class BottomTabBar extends LinearLayout {
    private FragmentManager manager;
    /**
     * 设置文本颜色
     */
    private int textColor ;
    /**
     * 设置文本选中颜色
     */
    private int textSelectColor  ;
    /**
     * 设置整体背景颜色
     */
    private int backgroundColor ;

    public void setManager(FragmentManager manager) {
        this.manager = manager;
    }

    public void setBars(List<BarEntity> bars) {
        init(bars);
    }

    public BottomTabBar(Context context) {
        this(context, null);
    }

    public BottomTabBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BottomTabBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs,R.styleable.BottomTabBar);
        textColor = typedArray.getColor(R.styleable.BottomTabBar_textColor,Color.parseColor("#666666"));
        textSelectColor = typedArray.getColor(R.styleable.BottomTabBar_textSelectColor,Color.parseColor("#666666"));
        backgroundColor = typedArray.getColor(R.styleable.BottomTabBar_backgroundColor,Color.parseColor("#e9e9e9"));
        typedArray.recycle();
    }

    /**
     * 初始化
     *
     * @param bars
     */
    private void init(final List<BarEntity> bars) {
        setOrientation(LinearLayout.VERTICAL);
        setBackgroundColor(backgroundColor);
        if (bars == null || bars.size() <= 0) {
            return;
        }
        /**
         * 最上面添加一个Fragment
         */
        FrameLayout fl = new FrameLayout(getContext());
        fl.setId(R.id.f_content);
        fl.setBackgroundColor(Color.parseColor("#ffffff"));
        LayoutParams flp = new LayoutParams(LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1);
        addView(fl, flp);
        /**
         * 添加一根华丽的分割线
         */
        View line = new View(getContext());
        line.setBackgroundColor(Color.parseColor("#cccccc"));
        ViewGroup.LayoutParams llp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,1);
        addView(line,llp);
        /**
         * 添加底部导航栏菜单
         */
        LinearLayout bottom = new LinearLayout(this.getContext());
        bottom.setOrientation(LinearLayout.HORIZONTAL);
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        for (int i = 0; i < bars.size(); i++) {
            LinearLayout bar = new LinearLayout(this.getContext());
            bar.setOrientation(LinearLayout.VERTICAL);
            LayoutParams blp = new LayoutParams(LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
            bar.setGravity(Gravity.CENTER);
            bar.setPadding(10,16,10,16);
            ImageView image = new ImageView(getContext());
            LayoutParams ilp = new LayoutParams((int) getResources().getDimension(R.dimen.image_height), (int) getResources().getDimension(R.dimen.image_height));
            image.setImageResource(bars.get(i).getTabUnSelectedResId());
            bar.addView(image, ilp);

            TextView text = new TextView(getContext());
            text.setText(bars.get(i).getTabText());
            LayoutParams tlp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
            text.setTextColor(textColor);
            bar.addView(text, tlp);

            final int position = i;
            bar.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    select(position, bars);
                }
            });
            bottom.addView(bar, blp);
        }
        addView(bottom, lp);
        /**
         * 默认选中第一个菜单栏
         */
        select(0, bars);
    }

    /**
     * 选择了第几个tab
     *
     * @param position
     */
    public void select(int position, List<BarEntity> bars) {
        if (getChildAt(1) == null) {
            return;
        }
        ImageView image;
        TextView text ;
        LinearLayout bottom = (LinearLayout) getChildAt(2);
        for (int i = 0; i < bottom.getChildCount(); i++) {
            LinearLayout bar = (LinearLayout) bottom.getChildAt(i);
            image = (ImageView) bar.getChildAt(0);
            text = (TextView) bar.getChildAt(1);
            if (position == i) {
                image.setImageResource(bars.get(i).getTabSelectedResId());
                text.setTextColor(textSelectColor);
            } else {
                image.setImageResource(bars.get(i).getTabUnSelectedResId());
                text.setTextColor(textColor);
            }
        }
        switchByPosition(position);
    }

    private void switchByPosition(int position) {
        if (onSelectListener!=null){
            onSelectListener.onSelect(position);
        }
    }

    private Fragment current;

    /**
     * 切换当前显示的fragment
     */
    public void switchContent(Fragment to) {
        if (current != to) {
            FragmentTransaction transaction = manager.beginTransaction();

            if (current != null) {
                transaction.hide(current);
            }
            if (!to.isAdded()) { // 先判断是否被add过
                transaction.add(R.id.f_content, to).commit();
            } else {

                transaction.show(to).commit(); // 隐藏当前的fragment，显示下一个
            }
            current = to;
        }
    }

    private OnSelectListener onSelectListener;
    /**
     * 选择切换的监听，在这里处理切换fragment,防止重复创建
     */
    public interface OnSelectListener{
        public void onSelect(int position);
    }

    public void setOnSelectListener(OnSelectListener onSelectListener) {
        this.onSelectListener = onSelectListener;
    }
}

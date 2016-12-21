package com.wzh.fun.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.wzh.fun.R;
import com.wzh.fun.chat.adapter.UserAdapter;

/**
 * Created by WZH on 2016/12/11.
 */

public class LoadMoreGridView extends LinearLayout {
    private View footer;
    private GridView gridView;
    private int mImageThumbSize;
    private int mImageThumbSpacing;
    private UserAdapter adapter ;
    public LoadMoreGridView(Context context) {
        this(context, null);
    }

    public LoadMoreGridView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadMoreGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setOrientation(VERTICAL);
        LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        setLayoutParams(lp);
        mImageThumbSize = getResources().getDimensionPixelSize(
                R.dimen.image_thumbnail_size);
        mImageThumbSpacing = getResources().getDimensionPixelSize(
                R.dimen.image_thumbnail_spacing);

        gridView = new GridView(getContext());
        gridView.setBackgroundColor(Color.parseColor("#ff00ff"));
        gridView.setColumnWidth((int) getResources().getDimension(R.dimen.image_thumbnail_size));
        gridView.setHorizontalSpacing((int) getResources().getDimension(R.dimen.image_thumbnail_spacing));
        gridView.setNumColumns(GridView.AUTO_FIT);
        gridView.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
        gridView.setVerticalSpacing((int) getResources().getDimension(R.dimen.image_thumbnail_spacing));
        LayoutParams lpg = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1);
        addView(gridView, lpg);

        footer = LayoutInflater.from(getContext()).inflate(R.layout.load_more_list_view_foot, null);
        LayoutParams lpf = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        addView(footer, lpf);
        AutoLoadListener autoLoadListener = new AutoLoadListener(callBack);
        gridView.setOnScrollListener(autoLoadListener);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(parent, view, position, id);
                }
            }
        });

    }

    AutoLoadListener.AutoLoadCallBack callBack = new AutoLoadListener.AutoLoadCallBack() {
        public void execute() {
            if (onLoadMoreListener != null) {
                onLoadMoreListener.loadMore();
            }
        }
    };

    public void setAdapter(final UserAdapter adapter) {
        this.adapter = adapter ;
        gridView.setAdapter(adapter);
        gridView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        final int numColumns = (int) Math.floor(gridView
                                .getWidth()
                                / (mImageThumbSize + mImageThumbSpacing));
                        if (numColumns > 0) {
                            int columnWidth = (gridView.getWidth() / numColumns)
                                    - mImageThumbSpacing;
                            adapter.setItemHeight(columnWidth);
                            gridView.getViewTreeObserver()
                                    .removeGlobalOnLayoutListener(this);
                        }
                    }
                });
        adapter.notifyDataSetChanged();
    }
    public UserAdapter getAdapter(){
        if (adapter!=null){
            return adapter ;
        }
        return  null ;
    }

    public OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        public void onItemClick(AdapterView<?> parent, View view, int position, long id);
    }

    public OnLoadMoreListener onLoadMoreListener;

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public interface OnLoadMoreListener {
        public void loadMore();
    }
}

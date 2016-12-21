package com.wzh.fun.view.tab;

/**
 * Created by zhihao.wen on 2016/11/4.
 */

public class BarEntity {
    private String tabText;
    private int tabSelectedResId ;
    private int tabUnSelectedResId ;

    public BarEntity(String tabText, int tabSelectedResId, int tabUnSelectedResId) {
        this.tabText = tabText;
        this.tabSelectedResId = tabSelectedResId;
        this.tabUnSelectedResId = tabUnSelectedResId;
    }

    public String getTabText() {
        return tabText;
    }

    public void setTabText(String tabText) {
        this.tabText = tabText;
    }

    public int getTabSelectedResId() {
        return tabSelectedResId;
    }

    public void setTabSelectedResId(int tabSelectedResId) {
        this.tabSelectedResId = tabSelectedResId;
    }

    public int getTabUnSelectedResId() {
        return tabUnSelectedResId;
    }

    public void setTabUnSelectedResId(int tabUnSelectedResId) {
        this.tabUnSelectedResId = tabUnSelectedResId;
    }
}


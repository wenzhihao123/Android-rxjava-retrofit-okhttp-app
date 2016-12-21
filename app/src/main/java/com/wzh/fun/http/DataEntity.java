package com.wzh.fun.http;

import com.google.gson.annotations.SerializedName;
import com.wzh.fun.entity.BaseEntity;

import java.util.List;

/**
 * Created by zhihao.wen on 2016/11/3.
 */

public class DataEntity<T> extends BaseEntity {
    @SerializedName("stat")
    private String stat ;
    @SerializedName("data")
    private List<T>  data ;

    public String getStat() {
        return stat;
    }

    public void setStat(String stat) {
        this.stat = stat;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "DataEntity{" +
                "stat='" + stat + '\'' +
                ", data=" + data +
                '}';
    }
}

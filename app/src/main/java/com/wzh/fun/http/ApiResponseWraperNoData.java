package com.wzh.fun.http;

import com.google.gson.annotations.SerializedName;
import com.wzh.fun.entity.BaseEntity;

import java.util.List;

/**
 * Created by zhihao.wen on 2016/11/3.
 */

public class ApiResponseWraperNoData<T> extends BaseEntity {
    @SerializedName("reason")
    private String reason ;
    @SerializedName("error_code")
    private String error_code ;
    @SerializedName("result")
    private List<T> result ;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getError_code() {
        return error_code;
    }

    public void setError_code(String error_code) {
        this.error_code = error_code;
    }

    public List<T> getResult() {
        return result;
    }

    public void setResult(List<T> result) {
        this.result = result;
    }
}

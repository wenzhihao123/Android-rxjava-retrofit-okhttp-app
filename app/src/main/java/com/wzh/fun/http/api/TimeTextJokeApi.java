package com.wzh.fun.http.api;

import com.wzh.fun.entity.TextJokeEntity;
import com.wzh.fun.http.ApiResponseWraper;
import com.wzh.fun.http.RequestParam;

import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by zhihao.wen on 2016/4/20.
 */
public interface TimeTextJokeApi {
    @GET("content/list.from")
    Observable<ApiResponseWraper<TextJokeEntity>> getTextJoke(@QueryMap RequestParam param);
}

package com.wzh.fun.http.api;

import com.wzh.fun.entity.ImageJokeEntity;
import com.wzh.fun.http.ApiResponseWraper;
import com.wzh.fun.http.RequestParam;

import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by zhihao.wen on 2016/4/20.
 */
public interface TimeImageJokeApi {
    @GET("img/list.from")
    Observable<ApiResponseWraper<ImageJokeEntity>> getImageJoke(@QueryMap RequestParam param);
}

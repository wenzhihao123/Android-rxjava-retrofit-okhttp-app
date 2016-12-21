package com.wzh.fun.http.api;

import com.wzh.fun.entity.ImageJokeEntity;
import com.wzh.fun.entity.WeatherEntity;
import com.wzh.fun.http.ApiResponseWraper;
import com.wzh.fun.http.ApiResponseWraperNoList;
import com.wzh.fun.http.RequestParam;

import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by zhihao.wen on 2016/11/23.
 */

public interface WeatherApi {
    @GET("query")
    Observable<ApiResponseWraperNoList<WeatherEntity>> getWeather(@QueryMap RequestParam param);
}


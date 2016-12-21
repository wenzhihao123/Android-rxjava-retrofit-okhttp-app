package com.wzh.fun.http;


import com.wzh.fun.http.api.ImageJokeApi;
import com.wzh.fun.http.api.TextJokeApi;
import com.wzh.fun.http.api.TimeImageJokeApi;
import com.wzh.fun.http.api.TimeTextJokeApi;
import com.wzh.fun.http.api.WeatherApi;
import com.wzh.fun.utils.Constant;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by zhihao.wen on 2016/4/20.
 */
public class NetWorkUtil {
    private static OkHttpClient okHttpClient;
    private static Converter.Factory gsonConverterFactory = GsonConverterFactory.create();
    private static CallAdapter.Factory rxJavaCallAdapterFactory = RxJavaCallAdapterFactory.create();
    private static TextJokeApi textJokeApi;
    private static TimeTextJokeApi timeTextJokeApi;
    private static ImageJokeApi imageJokeApi;
    private static TimeImageJokeApi timeImageJokeApi;
    private static WeatherApi weatherApi ;
    /**
     * 初始化okhttp
     */
    public static void initOkhttp() {
        if (okHttpClient == null) {
            HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            okHttpClient = new OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).build();
        }

    }

    /**
     * 获取最新文本笑话
     *
     * @return
     */
    public static TextJokeApi getTextJokeApi() {
        initOkhttp();
        if (textJokeApi == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Constant.RANDOM_BASE_URL)
                    .client(okHttpClient)
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .addConverterFactory(gsonConverterFactory)
                    .build();
            textJokeApi = retrofit.create(TextJokeApi.class);
        }
        return textJokeApi;
    }
    /**
     * 获取全部按时间文本笑话
     *
     * @return
     */
    public static TimeTextJokeApi getTimeTextJokeApi() {
        initOkhttp();
        if (timeTextJokeApi == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Constant.BASE_URL)
                    .client(okHttpClient)
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .addConverterFactory(gsonConverterFactory)
                    .build();
            timeTextJokeApi = retrofit.create(TimeTextJokeApi.class);
        }
        return timeTextJokeApi;
    }
    /**
     * 获取最新图片笑话
     *
     * @return
     */
    public static ImageJokeApi getImageJokeApi() {
        initOkhttp();
        if (imageJokeApi == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Constant.RANDOM_BASE_URL)
                    .client(okHttpClient)
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .addConverterFactory(gsonConverterFactory)
                    .build();
            imageJokeApi = retrofit.create(ImageJokeApi.class);
        }
        return imageJokeApi;
    }
    /**
     * 获取按时间排序的图片笑话
     *
     * @return
     */
    public static TimeImageJokeApi getTimeImageJokeApi() {
        initOkhttp();
        if (timeImageJokeApi == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Constant.BASE_URL)
                    .client(okHttpClient)
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .addConverterFactory(gsonConverterFactory)
                    .build();
            timeImageJokeApi = retrofit.create(TimeImageJokeApi.class);
        }
        return timeImageJokeApi;
    }

    /**
     * 获取天气数据
     * @return
     */
    public static WeatherApi getWeatherApi(){
        initOkhttp();
        if (weatherApi==null){
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Constant.BASE_WHEATHER)
                    .client(okHttpClient)
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .addConverterFactory(gsonConverterFactory)
                    .build();
            weatherApi = retrofit.create(WeatherApi.class);
        }
        return weatherApi;
    }

}

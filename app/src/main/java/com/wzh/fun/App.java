package com.wzh.fun;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVUser;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhihao.wen on 2016/11/25.
 */

public class App extends Application {
    public String appId = "u7MAWzHTM47y62viPU4Xv7pA-gzGzoHsz";
    public String appKey = "OgXum0wNciPhV8jk1Npp97I5";
    public static final String LIKES = "likes";
    public static final String STATUS_DETAIL = "StatusDetail";
    public static final String DETAIL_ID = "detailId";
    public static final String CREATED_AT = "createdAt";
    public static final String FOLLOWER = "follower";
    public static final String FOLLOWEE = "followee";

    @Override
    public void onCreate() {
        super.onCreate();
        PlatformConfig.setWeixin("wx967daebe835fbeac", "5bb696d9ccd75a38c8a0bfe0675559b3");
        PlatformConfig.setSinaWeibo("1642428042", "ce6ecc836cbdebd1c3b55fd6a6f0565c");
        PlatformConfig.setQQZone("1105900060", "JmVyiyl2rB1XSpvW");
        UMShareAPI.get(this);
        AVOSCloud.initialize(this,appId,appKey);
//        AVIMMessageManager.registerDefaultMessageHandler(new CustomMessageHandler());
    }
    public static Map<String, AVUser> userCache = new HashMap<>();

    public static void regiserUser(AVUser user) {
        userCache.put(user.getObjectId(), user);
    }

    public static void registerBatchUser(List<AVUser> users) {
        for (AVUser user : users) {
            regiserUser(user);
        }
    }

    public static AVUser lookupUser(String userId) {
        return userCache.get(userId);
    }



}

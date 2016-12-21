package com.wzh.fun.chat;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVStatus;
import com.avos.avoscloud.AVStatusQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.FollowCallback;
import com.avos.avoscloud.SaveCallback;
import com.wzh.fun.App;
import com.wzh.fun.chat.entity.Status;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by zhihao.wen on 2016/11/29.
 */

public class CircleServerUtils {
    public static final int MUTUAL_FOLLOW = 0;//disable follow
    public static final int FOLLOWER = 1;  //can follow
    public static final int FOLLOWING = 2;  //disable follow
    public static final int NONE_FOLLOW = 3; //can follow

    public static void sendStatus(final String text, Bitmap bitmap, final SaveCallback saveCallback) {
        if (bitmap != null) {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);
            byte[] bs = out.toByteArray();
            AVUser user = AVUser.getCurrentUser();
            String name = user.getUsername() + "_" + System.currentTimeMillis();
            final AVFile file = new AVFile(name, bs);
            file.saveInBackground(new SaveCallback() {
                @Override
                public void done(AVException e) {
                    if (e != null) {
                        saveCallback.done(e);
                    } else {
                        String url = file.getUrl();
                        sendStatus(text, url, saveCallback);
                    }
                }
            });
        } else {
            sendStatus(text, "", saveCallback);
        }
    }

    public static void sendStatus(final String text, final String url, final SaveCallback saveCallback) {
        final AVObject statusDetail = new AVObject(App.STATUS_DETAIL);
        statusDetail.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e != null) {
                    saveCallback.done(e);
                } else {
                    AVStatus status = new AVStatus();
                    status.setMessage(text);
                    status.setImageUrl(url);
                    Map<String, Object> datas = new HashMap<String, Object>();
                    datas.put(App.DETAIL_ID, statusDetail.getObjectId());
                    status.setData(datas);
                    AVStatus.sendStatusToFollowersInBackgroud(status, saveCallback);
                }
            }
        });
    }

    public static void getStatusDatas(final long maxId, final int limit, Subscriber sb) throws AVException {
        Observable.create(new Observable.OnSubscribe<List<Status>>() {
            @Override
            public void call(Subscriber<? super List<Status>> subscriber) {
                AVUser user = AVUser.getCurrentUser();
                AVStatusQuery q = AVStatus.inboxQuery(user, AVStatus.INBOX_TYPE.TIMELINE.toString());
                q.setLimit(limit);
                q.setMaxId(maxId);
                q.orderByDescending(App.CREATED_AT);
                List<AVStatus> avStatuses = null;
                try {
                    if (avStatuses == null) {
                        avStatuses = new ArrayList<AVStatus>();
                    }
                    avStatuses = q.find();
                    subscriber.onNext(fetchDetailsAndGetStatuses(avStatuses));
                } catch (AVException e) {
                    e.printStackTrace();
                }

            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(sb);
//
        Log.e("main", "getStatusDatas");
//        return fetchDetailsAndGetStatuses(avStatuses);
    }

    public static boolean filterException(Context ctx, Exception e) {
        if (e != null) {
            Toast.makeText(ctx, e.getMessage(), Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    public static List<Status> fetchDetailsAndGetStatuses(List<AVStatus> avStatuses) throws AVException {
        List<AVObject> objects = new ArrayList<AVObject>();
        for (AVStatus status : avStatuses) {
            Map<String, Object> data = status.getData();
            String detailId = (String) data.get(App.DETAIL_ID);
            AVObject object = AVObject.createWithoutData(App.STATUS_DETAIL, detailId);
            objects.add(object);
        }
        List<AVObject> avObjects = AVObject.fetchAll(objects);
        List<Status> statuses = new ArrayList<Status>();
        for (int i = 0; i < avStatuses.size(); i++) {
            AVStatus avStatus = avStatuses.get(i);
            AVObject avObject = avObjects.get(i);
            Status status = new Status();
            status.setInnerStatus(avStatus);
            status.setDetail(avObject);
            statuses.add(status);
        }
        return statuses;
    }

    public static void getUserStatusList(final AVUser user, final int skip, final int limit, Subscriber sb) throws AVException {
        Observable.create(new Observable.OnSubscribe<List<Status>>() {
            @Override
            public void call(Subscriber<? super List<Status>> subscriber) {
                AVStatusQuery q = null;
                try {
                    q = AVStatus.statusQuery(user);
                } catch (AVException e) {
                    e.printStackTrace();
                }
                q.orderByDescending(App.CREATED_AT);
                q.setSkip(skip);
                q.setLimit(limit);
                List<AVStatus> avStatuses = null;
                try {
                    if (avStatuses == null) {
                        avStatuses = new ArrayList<AVStatus>();
                    }
                    avStatuses = q.find();
                    subscriber.onNext(fetchDetailsAndGetStatuses(avStatuses));
                } catch (AVException e) {
                    e.printStackTrace();
                }

            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(sb);

    }

    public static void getFollowers(final AVUser user, final int skip, final int limit, Subscriber sb) throws Exception {
        Observable.create(new Observable.OnSubscribe<List<AVUser>>() {
            @Override
            public void call(Subscriber<? super List<AVUser>> subscriber) {
                AVQuery<AVUser> q = AVUser.followerQuery(user.getObjectId(),AVUser.class);
                q.skip(skip);
                q.limit(limit);
                q.include(App.FOLLOWER);
                try {
                    subscriber.onNext(q.find());
                } catch (AVException e) {
                    e.printStackTrace();
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(sb);
    }

    public static void getFollowings(final AVUser user, final int skip, final int limit,Subscriber sb) throws Exception {
        Observable.create(new Observable.OnSubscribe<List<AVUser>>() {
            @Override
            public void call(Subscriber<? super List<AVUser>> subscriber) {
                AVQuery<AVUser> q = AVUser.followeeQuery(user.getObjectId(),AVUser.class);
                q.skip(skip);
                q.limit(limit);
                q.include(App.FOLLOWEE);
                try {
                    subscriber.onNext(q.find());
                } catch (AVException e) {
                    e.printStackTrace();
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(sb);

    }

    public static int followStatus(AVUser user) throws AVException {
        boolean isMyFollower = findFollowStatus(user, true);
        boolean isMyFollowing = findFollowStatus(user, false);
        if (isMyFollower && isMyFollowing) {
            return MUTUAL_FOLLOW;
        } else if (isMyFollower) {
            return FOLLOWER;
        } else if (isMyFollowing) {
            return FOLLOWING;
        } else {
            return NONE_FOLLOW;
        }
    }
    public static boolean findFollowStatus(AVUser user, boolean askFollower) throws AVException {
        AVUser currentUser = AVUser.getCurrentUser();
        AVQuery<AVUser> q;
        if (askFollower) {
            q = AVUser.followerQuery(currentUser.getObjectId(), AVUser.class);
            q.whereEqualTo(App.FOLLOWER, user);
        } else {
            q = AVUser.followeeQuery(currentUser.getObjectId(), AVUser.class);
            q.whereEqualTo(App.FOLLOWEE, user);
        }
        q.setLimit(1);
        List<AVUser> avUsers = q.find();
        return avUsers.isEmpty() == false;
    }

    public static void followAction(AVUser user, boolean follow, FollowCallback followCallback) {
        AVUser currentUser = AVUser.getCurrentUser();
        if (follow) {
            currentUser.followInBackground(user.getObjectId(), followCallback);
        } else {
            currentUser.unfollowInBackground(user.getObjectId(), followCallback);
        }
    }

    public static void saveStatusLikes(AVObject detail, List<String> likes, SaveCallback saveCallback) {
        detail.put(App.LIKES, likes);
        detail.saveInBackground(saveCallback);
    }

    public static void deleteStatus(Status status) throws AVException {
        AVStatus innerStatus = status.getInnerStatus();
        innerStatus.delete();
        AVObject detail = status.getDetail();
        detail.delete();
    }

    public static void findUsers(final int skip, final int limit, final Subscriber sb) throws AVException {
        Observable.create(new Observable.OnSubscribe<List<AVUser>>() {
            @Override
            public void call(Subscriber<? super List<AVUser>> subscriber) {
                AVQuery<AVUser> q = AVUser.getQuery();
                q.skip(skip);
                q.limit(limit);
                q.orderByDescending("updatedAt");
                try {
                    subscriber.onNext(q.find());
                } catch (AVException e) {
                    e.printStackTrace();
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(sb);

    }
}

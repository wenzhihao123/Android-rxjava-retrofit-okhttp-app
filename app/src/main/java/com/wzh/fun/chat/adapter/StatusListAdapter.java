package com.wzh.fun.chat.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVStatus;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;
import com.bumptech.glide.Glide;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.ShareContent;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.shareboard.ShareBoardConfig;
import com.wzh.fun.App;
import com.wzh.fun.R;
import com.wzh.fun.chat.CircleServerUtils;
import com.wzh.fun.chat.activity.ImageLookActivity;
import com.wzh.fun.chat.entity.Status;
import com.wzh.fun.chat.fragment.PersonFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by lzw on 15/1/2.
 */
public class StatusListAdapter extends BaseListAdapter<Status> {

  public StatusListAdapter(Context ctx) {
    super(ctx);
  }

  @Override
  public View getView(int position, View conView, ViewGroup parent) {
    ViewHolder viewHolder = null ;
    if (conView == null) {
        viewHolder = new ViewHolder();
        conView = inflater.inflate(R.layout.status_item, null, false);
        viewHolder.nameView = (TextView) conView.findViewById(R.id.nameView);
        viewHolder.textView = (TextView) conView.findViewById(R.id.statusText);
        viewHolder.avatarView = (ImageView) conView.findViewById(R.id.avatarView);
        viewHolder.imageView = (ImageView) conView.findViewById( R.id.statusImage);
        viewHolder.likeView = (ImageView) conView.findViewById(R.id.likeView);
        viewHolder.likeCountView = (TextView) conView.findViewById(R.id.likeCount);
        viewHolder.likeLayout = conView.findViewById( R.id.likeLayout);
        viewHolder.timeView = (TextView) conView.findViewById( R.id.timeView);
        viewHolder.share= conView.findViewById(R.id.share);
        conView.setTag(viewHolder);
    }else {
      viewHolder = (ViewHolder) conView.getTag();
    }

    final Status status = datas.get(position);
    final AVStatus innerStatus = status.getInnerStatus();
    AVUser source = innerStatus.getSource();
    Log.e("main", "--->"+source.getUsername() );
    AVFile file = source.getAVFile("avatar");
    if (file != null) {
      Glide.with(ctx).load(file.getUrl()).placeholder(R.drawable.default_head).into(viewHolder.avatarView);
    }else {
      viewHolder.avatarView.setImageResource(R.drawable.default_head);
    }
    viewHolder.nameView.setText(source.getUsername());

    viewHolder.avatarView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        PersonFragment.go(ctx, innerStatus.getSource());
      }
    });

    if (TextUtils.isEmpty(innerStatus.getMessage())) {
      viewHolder.textView.setVisibility(View.GONE);
    } else {
      viewHolder.textView.setText(innerStatus.getMessage());
      viewHolder.textView.setVisibility(View.VISIBLE);}
    if (TextUtils.isEmpty(innerStatus.getImageUrl()) == false) {
      viewHolder.imageView.setVisibility(View.VISIBLE);
      Glide.with(ctx).load(innerStatus.getImageUrl()).into(viewHolder.imageView);
      viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          Intent intent = new Intent(ctx, ImageLookActivity.class);
          intent.putExtra("url",innerStatus.getImageUrl());
          ctx.startActivity(intent);
        }
      });
    } else {
      viewHolder.imageView.setVisibility(View.GONE);
    }
    final AVObject detail = status.getDetail();

    final List<String> likes;
    if (detail.get(App.LIKES) != null) {
      likes = (List<String>) detail.get(App.LIKES);
    } else {
      likes = new ArrayList<String>();
    }

    int n = likes.size();
    if (n > 0) {
      viewHolder.likeCountView.setText(n + "");
    } else {
      viewHolder.likeCountView.setText("");
    }

    final AVUser user = AVUser.getCurrentUser();
    final String userId = user.getObjectId();
    final boolean contains = likes.contains(userId);
    if (contains) {
      viewHolder.likeView.setImageResource(R.drawable.status_ic_player_liked);
    } else {
      viewHolder.likeView.setImageResource(R.drawable.ic_player_like);
    }
    viewHolder.likeLayout.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        SaveCallback saveCallback = new SaveCallback() {
          @Override
          public void done(AVException e) {
            if (CircleServerUtils.filterException(ctx, e)) {
              notifyDataSetChanged();
            }
          }
        };
        if (contains) {
          likes.remove(userId);
          CircleServerUtils.saveStatusLikes(detail, likes, saveCallback);
        } else {
          likes.add(userId);
          CircleServerUtils.saveStatusLikes(detail, likes, saveCallback);
        }
      }
    });

    viewHolder.timeView.setText(millisecs2DateString(innerStatus.getCreatedAt().getTime()));
    viewHolder.share.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
         share(status);
      }
    });
    return conView;
  }

  private void share(Status status) {
    ShareContent sc = new ShareContent();
    sc.mText = status.getInnerStatus().getMessage();
    sc.mTargetUrl = "http://sj.qq.com/myapp/";
    sc.mTitle = "段子手的笑话分享" ;

    ShareBoardConfig  sbc = new ShareBoardConfig();
    sbc.setTitleVisibility(false);

    new ShareAction((Activity)ctx).setShareContent(sc).withMedia(new UMImage(ctx,status.getInnerStatus().getImageUrl()))
            .setDisplayList(SHARE_MEDIA.SINA,SHARE_MEDIA.QQ,SHARE_MEDIA.QZONE,SHARE_MEDIA.WEIXIN,SHARE_MEDIA.WEIXIN_CIRCLE)
            .setCallback(umShareListener).open(sbc);
  }
  private UMShareListener umShareListener = new UMShareListener() {
      @Override
      public void onResult(SHARE_MEDIA platform) {
        Log.d("plat","platform"+platform);
        Toast.makeText(ctx, platform + " 分享成功啦", Toast.LENGTH_SHORT).show();

      }

      @Override
      public void onError(SHARE_MEDIA platform, Throwable t) {
        Toast.makeText(ctx,platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
        if(t!=null){
          Log.d("throw","throw:"+t.getMessage());
        }
      }

      @Override
      public void onCancel(SHARE_MEDIA platform) {
        Toast.makeText(ctx,platform + " 分享取消了", Toast.LENGTH_SHORT).show();
      }
  };


  public class ViewHolder{
    TextView nameView ;
    TextView textView ;
    ImageView avatarView;
    ImageView imageView ;
    ImageView likeView ;
    TextView likeCountView ;
    View likeLayout,share ;
    TextView timeView;

  }

  public static String getDate(Date date) {
    SimpleDateFormat format = new SimpleDateFormat("MM-dd HH:mm");
    return format.format(date);
  }

  public static String millisecs2DateString(long timestamp) {
//    long gap = System.currentTimeMillis() - timestamp;
//    if (gap < 1000 * 60 * 60 * 24) {
//      String s = prettyTime.format(new Date(timestamp));
//      return s.replace(" ", "");
//    } else {
//      return getDate(new Date(timestamp));
//    }
    return getDate(new Date(timestamp));
  }

}

package com.wzh.fun.adapter;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.ShareContent;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.shareboard.ShareBoardConfig;
import com.wzh.fun.R;
import com.wzh.fun.entity.ImageJokeEntity;
import com.wzh.fun.entity.TextJokeEntity;
import com.wzh.fun.utils.DateUtils;

import java.util.List;

/**
 * Created by zhihao.wen on 2016/11/3.
 */

public class ImageJokeAdapter extends BaseAdapter {
    private List<ImageJokeEntity> imageJokeEntities ;
    private Context ctx ;
    public ImageJokeAdapter(Context mContext, List<ImageJokeEntity> imageJokeEntities) {
        this.ctx = mContext ;
        this.imageJokeEntities = imageJokeEntities ;
    }

    @Override
    public int getCount() {
        return imageJokeEntities.size();
    }

    @Override
    public ImageJokeEntity getItem(int position) {
        return imageJokeEntities.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        ViewHolder holder = null ;
        final ImageJokeEntity imageJokeEntity = imageJokeEntities.get(position);
        if (convertView==null){
            convertView = LayoutInflater.from(ctx).inflate(R.layout.item_imagejoke_list_item,null);
            holder = new ViewHolder();
            holder.title = (TextView) convertView.findViewById(R.id.title);
            holder.time = (TextView) convertView.findViewById(R.id.time);
            holder.image = (SimpleDraweeView) convertView.findViewById(R.id.image);
            holder.share = convertView.findViewById(R.id.share);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.time.setText("更新时间："+ DateUtils.getStrTime(imageJokeEntity.getUnixtime()+""));
        holder.title.setText(imageJokeEntity.getContent());
        DraweeController draweeController = Fresco.newDraweeControllerBuilder()
                .setAutoPlayAnimations(true)//自动播放动画
                .setUri(Uri.parse(imageJokeEntity.getUrl()))//路径
                .build();
        holder.image.setController(draweeController);
        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                share(imageJokeEntity);
            }
        });
        return convertView;
    }
    public static class ViewHolder{
        private TextView title;
        private TextView time;
        private SimpleDraweeView image ;
        private View share ;
    }
    private void share(ImageJokeEntity joke) {
        ShareContent sc = new ShareContent();
        sc.mText = joke.getContent();
        sc.mTargetUrl = "http://sj.qq.com/myapp/";
        sc.mTitle = "段子手的笑话分享" ;

        ShareBoardConfig sbc = new ShareBoardConfig();
        sbc.setTitleVisibility(false);

        new ShareAction((Activity)ctx).setShareContent(sc).withMedia(new UMImage(ctx,joke.getUrl()))
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
}

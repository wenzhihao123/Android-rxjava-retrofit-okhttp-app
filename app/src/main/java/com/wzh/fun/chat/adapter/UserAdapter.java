package com.wzh.fun.chat.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVStatus;
import com.avos.avoscloud.AVUser;
import com.bumptech.glide.Glide;
import com.wzh.fun.R;
import com.wzh.fun.chat.entity.Status;

import java.util.List;

/**
 * Created by WZH on 2016/12/3.
 */

public class UserAdapter extends BaseListAdapter<AVUser>  {
    private int mItemHeight ;
    public UserAdapter(Context ctx) {
        super(ctx);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder ;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_user_list, null, false);
            viewHolder.avatarView = (ImageView) convertView.findViewById(R.id.avatarView);
            viewHolder.nameView = (TextView) convertView.findViewById(R.id.nameView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (viewHolder.avatarView.getLayoutParams().height != mItemHeight) {
            viewHolder.avatarView.getLayoutParams().height = mItemHeight;
        }
        final AVUser user = datas.get(position);
        AVFile file = user.getAVFile("avatar");
        if (file != null) {
            Glide.with(ctx).load(file.getUrl()).placeholder(R.drawable.default_load).into(viewHolder.avatarView);
        }else {
            Glide.with(ctx).load(R.drawable.default_square).into(viewHolder.avatarView);
        }
        viewHolder.nameView.setText(user.getUsername());
        return convertView;
    }
    public class ViewHolder{
        TextView nameView;
        ImageView avatarView;
    }
    /**
     * 设置item子项的高度。
     */
    public void setItemHeight(int height) {
        if (height == mItemHeight) {
            return;
        }
        mItemHeight = height;
        notifyDataSetChanged();
    }
}

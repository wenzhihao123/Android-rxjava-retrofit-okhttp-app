package com.wzh.fun.chat.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.SaveCallback;
import com.bumptech.glide.Glide;
import com.wzh.fun.R;
import com.wzh.fun.chat.CircleServerUtils;
import com.wzh.fun.ui.activity.BaseActivity;
import com.wzh.fun.ui.fragment.BaseFragment;
import com.wzh.fun.utils.GlideCircleTransform;
import com.wzh.fun.utils.PermissionUtils;
import com.wzh.fun.view.LoadingDialog;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import me.iwf.photopicker.PhotoPagerActivity;
import me.iwf.photopicker.PhotoPickerActivity;
import me.iwf.photopicker.utils.PhotoPickerIntent;

import static android.app.Activity.RESULT_OK;

/**
 * Created by lzw on 15/1/2.
 */
public class StatusSendActivity extends BaseActivity implements View.OnClickListener{
    private static final int IMAGE_PICK_REQUEST = 0;
    private static final int SEND_REQUEST = 2;
    private Context context;
    private EditText editText;
    private ImageView imageView;
    private View addImage ;
    private Bitmap bitmap;
    private String path;
    private int REQUEST_CODE = 100;
    private LoadingDialog dialog ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.status_send_layout);
        setTitle("发布段子");
        setBackView();
        initView();
    }

    private void initView() {
        editText = (EditText) findViewById(R.id.editText);
        imageView = (ImageView) findViewById(R.id.image);
        addImage = findViewById(R.id.imageAction);
        addImage.setOnClickListener(this);
        dialog = new LoadingDialog(this);
        setRightText("发送", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                send();
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(path)){
                    ArrayList<String> paths = new ArrayList<String>();
                    paths.add(path);
                    Intent intent = new Intent(StatusSendActivity.this, PhotoPagerActivity.class);
                    intent.putExtra(PhotoPagerActivity.EXTRA_PHOTOS, paths);
                    startActivityForResult(intent, REQUEST_CODE);
                }
            }
        });
    }

    public void send() {
        String text = editText.getText().toString();
        if (TextUtils.isEmpty(text)){
            Toast.makeText(StatusSendActivity.this,"忘了说点什么吧！",Toast.LENGTH_SHORT).show();
            return;
        }
        dialog.show();
        dialog.setMessage("正在生成，请稍候");
        if (TextUtils.isEmpty(text) == false || bitmap != null) {
            CircleServerUtils.sendStatus(text, bitmap, new SaveCallback() {
                @Override
                public void done(AVException e) {
                    if (CircleServerUtils.filterException(context, e)) {
                       setResult(RESULT_OK);
                        dialog.dismiss();
                        finish();
                    }
                }
            });
        }
    }

    public void pickImage() {
        PermissionUtils.requestPermission(this, PermissionUtils.CODE_CAMERA, mPermissionGrant);
        PhotoPickerIntent intent = new PhotoPickerIntent(this);
        intent.setPhotoCount(9);
        intent.setShowCamera(true);
        startActivityForResult(intent, REQUEST_CODE);
    }
    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        PermissionUtils.requestPermissionsResult(this, requestCode, permissions, grantResults, mPermissionGrant);
    }

    private PermissionUtils.PermissionGrant mPermissionGrant = new PermissionUtils.PermissionGrant() {
        @Override
        public void onPermissionGranted(int requestCode) {
            switch (requestCode) {
                case PermissionUtils.CODE_CAMERA:
//                    Toast.makeText(StatusSendActivity.this, "Result Permission Grant CODE_CAMERA", Toast.LENGTH_SHORT).show();
                    break;
                case PermissionUtils.CODE_READ_EXTERNAL_STORAGE:
//                    Toast.makeText(StatusSendActivity.this, "Result Permission Grant CODE_READ_EXTERNAL_STORAGE", Toast.LENGTH_SHORT).show();
                    break;
                case PermissionUtils.CODE_WRITE_EXTERNAL_STORAGE:
//                    Toast.makeText(StatusSendActivity.this, "Result Permission Grant CODE_WRITE_EXTERNAL_STORAGE", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (data != null) {
                ArrayList<String> photos =
                        data.getStringArrayListExtra(PhotoPickerActivity.KEY_SELECTED_PHOTOS);
                if (photos == null) {
                    path = null ;
                    imageView.setImageResource(R.drawable.default_load);
                    return;
                }
                path = photos.get(0);
                if (!TextUtils.isEmpty(path)) {
                    bitmap = BitmapFactory.decodeFile(path);
                    Glide.with(StatusSendActivity.this).load(new File(path)).placeholder(R.drawable.default_load).into(imageView);
                }
            }else {
                imageView.setImageResource(R.drawable.default_load);
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==addImage.getId()){
            pickImage();
        }
    }
}

package com.wzh.fun.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.wzh.fun.R;

import retrofit2.http.POST;

/**
 * Created by zhihao.wen on 2016/11/24.
 */

public class AirPMView extends View {
    private Paint paint ;
    private Path path ;
    private LinearGradient  sd;
    private int width = 400 ;
    private int height = 60;
    private RectF rectF ;
//    private int colors[] = new int[]{Color.parseColor("#6BCD07"),Color.parseColor("#FCD12D"),Color.parseColor("#FE8802")};
    private int colors[] = new int[]{Color.parseColor("#6BCD07"),Color.parseColor("#FCD12D"),Color.parseColor("#FE8802"),Color.parseColor("#FE0000"),Color.parseColor("#CC0001"),Color.parseColor("#970454"),Color.parseColor("#62001E")};
    public AirPMView(Context context) {
        this(context,null);
    }

    public AirPMView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public AirPMView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        intit();
    }

    private void intit() {
        rectF = new RectF();
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(ContextCompat.getColor(getContext(), R.color.black));
//        sd = new LinearGradient(-width, 0, 0, 0,
//                new int[] { 0x33ffffff, 0xffffffff, 0x33ffffff },
//                new float[] { 0, 0.5f, 1 }, Shader.TileMode.CLAMP);
        sd = new LinearGradient(getPaddingLeft(),getTop()+15,getResources().getDisplayMetrics().widthPixels-getPaddingRight(),getTop()+getPaddingTop()+15,colors,null, Shader.TileMode.MIRROR);
//        sd = new LinearGradient(getPaddingLeft(),75,width-getPaddingRight(),75, colors,positions, Shader.TileMode.REPEAT);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        rectF.set(getPaddingLeft(),getTop()+getPaddingTop(),width-getPaddingRight(),getTop()+getPaddingTop()+30);
        paint.setShader(sd);
        canvas.drawRoundRect(rectF,15,15,paint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        }else {
            width = width ;
        }
        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        }else {
            height = height ;
        }

        setMeasuredDimension(width,height);



    }
}

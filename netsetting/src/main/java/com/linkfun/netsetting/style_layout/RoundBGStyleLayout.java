package com.linkfun.netsetting.style_layout;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.RectF;
import android.util.AttributeSet;

import com.linkfun.netsetting.R;
import com.linkfun.netsetting.utils.SQLUtils;
import com.zhy.android.percent.support.PercentRelativeLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class RoundBGStyleLayout extends PercentRelativeLayout {
    private  int mLayoutColorTwe=Color.GREEN;
    private  int mRadius=15;
    private  int mLayoutColorOne =Color.WHITE;
    private  int mLayoutColor ;
    int mDirection=-1;
    public RoundBGStyleLayout(Context context) {
        super(context);
        setWillNotDraw(false);
    }

    public RoundBGStyleLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        setWillNotDraw(false);
        EventBus.getDefault().register(this);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RoundBGStyleLayout);
        mRadius = typedArray.getDimensionPixelSize(R.styleable.RoundBGStyleLayout_RoundBGStyleLayout_Radius, 15);
        mLayoutColorOne = typedArray.getColor(R.styleable.RoundBGStyleLayout_RoundBGStyleLayout_Style_One, Color.WHITE);
        mLayoutColorTwe =typedArray.getColor(R.styleable.RoundBGStyleLayout_RoundBGStyleLayout_Style_Twe,Color.GREEN);
        mDirection =typedArray.getInteger(R.styleable.RoundBGStyleLayout_RoundBGStyleLayout_Direction,-1);
        mLayoutColor=mLayoutColorOne;
        int style = SQLUtils.getStyleNumber(context);
        if (style==1){
            applyStyle(mLayoutColorOne);
        }
        if (style==2){
            applyStyle(mLayoutColorTwe);
        }
    }


    public RoundBGStyleLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }
    Paint bg_paint=new Paint();//创建画笔
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        bg_paint.setStyle(Paint.Style.FILL);//设置空心
        bg_paint.setColor(mLayoutColor);//为画笔设置颜色
        RectF bgRect=new RectF(0,0,this.getWidth(),this.getHeight());
        canvas.drawRoundRect(bgRect,mRadius,mRadius,bg_paint);
        if (mDirection==1){
            int bgHeadHeight=50;
            RectF bgBottomHeadRect=new RectF(0,this.getHeight()- bgHeadHeight,this.getWidth(),this.getHeight());
            bg_paint.setColor(mLayoutColor);
            canvas.drawRect(bgBottomHeadRect,bg_paint);
        }
        if (mDirection==2){
            int bgHeadHeight=50;
            RectF bgBottomHeadRect=new RectF(0,0,this.getWidth(),bgHeadHeight);
            bg_paint.setColor(mLayoutColor);
            canvas.drawRect(bgBottomHeadRect,bg_paint);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        EventBus.getDefault().unregister(this);
        super.onDetachedFromWindow();
    }
    public  void applyStyle(int styleColor ){
      mLayoutColor=styleColor;
      this.invalidate();
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(StyleChangeEvent event) {
        int style = event.getStyle();
        if (style==1){
            applyStyle(mLayoutColorOne);
        }
        if (style==2){
            applyStyle(mLayoutColorTwe);
        }
    };
}

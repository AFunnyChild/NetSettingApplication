package com.linkfun.netsetting.style_layout;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.linkfun.netsetting.R;
import com.linkfun.netsetting.utils.SQLUtils;
import com.zhy.android.percent.support.PercentRelativeLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class RelativeStyleLayout extends PercentRelativeLayout {
    private int mStyleBGOne;
    private int mStyleBGTwe;
    public RelativeStyleLayout(Context context) {
        super(context);
    }
    public RelativeStyleLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context,attrs);
    }
    public RelativeStyleLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
       initView(context,attrs);
    }
    private void initView(Context context, AttributeSet attrs) {
        EventBus.getDefault().register(this);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RelativeStyleLayout);
        mStyleBGOne = typedArray.getResourceId(R.styleable.RelativeStyleLayout_RelativeStyleLayout_Style_One, -1);
        mStyleBGTwe = typedArray.getResourceId(R.styleable.RelativeStyleLayout_RelativeStyleLayout_Style_Twe, -1);
        applyStyle(mStyleBGOne);
        int style = SQLUtils.getStyleNumber(context);
        if (style==1){
            applyStyle(mStyleBGOne);
        }
        if (style==2){
            applyStyle(mStyleBGTwe);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        EventBus.getDefault().unregister(this);
        super.onDetachedFromWindow();
    }

    public  void applyStyle(int styleRes ){
        if (styleRes!=-1){
            setBackgroundResource(styleRes);
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(StyleChangeEvent event) {
        int style = event.getStyle();
        if (style==1){
            applyStyle(mStyleBGOne);
        }
        if (style==2){
            applyStyle(mStyleBGTwe);
        }
    };
}

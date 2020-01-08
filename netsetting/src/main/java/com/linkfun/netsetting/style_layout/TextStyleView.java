package com.linkfun.netsetting.style_layout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.TextView;

import com.linkfun.netsetting.R;
import com.linkfun.netsetting.utils.SQLUtils;
import com.zhy.android.percent.support.PercentLinearLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@SuppressLint("AppCompatCustomView")
public class TextStyleView extends TextView {
    private int mStyleBGOne;
    private int mStyleBGTwe;

    public TextStyleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context,attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        EventBus.getDefault().register(this);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TextStyleView);
        mStyleBGOne = typedArray.getColor(R.styleable.TextStyleView_TextStyleView_Style_One, Color.parseColor("#111111"));
        mStyleBGTwe = typedArray.getColor(R.styleable.TextStyleView_TextStyleView_Style_Twe,Color.parseColor("#FAC03D"));
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

            setTextColor(styleRes);

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

package com.linkfun.netsetting.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.linkfun.netsetting.R;
import com.zhy.android.percent.support.PercentRelativeLayout;


public class SettingItemView extends PercentRelativeLayout {




    public SettingItemView(Context context) {
        this(context, null);
    }

    public SettingItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        // 导入布局
        LayoutInflater.from(context).inflate(R.layout.item_setting, this, true);
        ImageView ivRight = (ImageView) findViewById(R.id.iv_right);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SettingItemView);
        boolean  mRightVisible = typedArray.getBoolean(R.styleable.SettingItemView_SettingItemView_RightVisible, false);
        if (mRightVisible){
            ivRight.setVisibility(VISIBLE);

        }else{
            ivRight.setVisibility(View.GONE);
        }
    }


}

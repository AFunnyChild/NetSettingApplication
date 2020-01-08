package com.linkfun.netsetting;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.util.AttributeSet;
import android.util.Log;

import com.zhy.android.percent.support.PercentRelativeLayout;

import java.util.ArrayList;
import java.util.List;

public class PasswordCircleLayout extends PercentRelativeLayout {

   int mLineHeight =5;
   int mCircleHeight =30;
   int mCircleInterval =40;
   List<Integer>  mPasswordList=new ArrayList<>();
    public PasswordCircleLayout(Context context) {
        super(context);
        setWillNotDraw(false);
        initView(context);

    }

    public List<Integer> getPasswordList() {
        return mPasswordList;
    }

    public void removePassword() {
  if (mPasswordList.size()>0){
      mPasswordList.remove(mPasswordList.size()-1);
  }
        this.invalidate();
    }

    public void addPassword(int passwordItem) {
      if (mPasswordList.size()<=5){
          mPasswordList.add(passwordItem);
      }
      this.invalidate();
    }

    private void initView(Context context) {
        mLineHeight =   dip2px(context,1);
        mCircleHeight=dip2px(context,15);
        mCircleInterval=dip2px(context,21);

    }

    public PasswordCircleLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);
        initView(context);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mCircleHeight=h/2-mLineHeight;
    }

    public PasswordCircleLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setWillNotDraw(false);
        initView(context);
    }
    Paint bg_paint=new Paint();//创建画笔
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        bg_paint.setStyle(Paint.Style.FILL);//设置空心
        bg_paint.setColor(Color.WHITE);
        bg_paint.setStrokeWidth(mLineHeight);
        canvas.drawLine(0,this.getHeight()- mLineHeight,this.getWidth(),this.getHeight()- mLineHeight,bg_paint);

        int passwordSize=mPasswordList.size();
        if (passwordSize>0){
            int circleStartX=(this.getWidth()- (passwordSize*mCircleHeight+(passwordSize-1)*mCircleInterval))/2+mCircleHeight/2;

            for (int i = 0; i < mPasswordList.size(); i++) {

                canvas.drawCircle(circleStartX+(mCircleInterval+mCircleHeight)*i,mCircleHeight,mCircleHeight/2,bg_paint);
            }
        }



    }
    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}

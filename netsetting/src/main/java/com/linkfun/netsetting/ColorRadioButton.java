package com.linkfun.netsetting;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Rect;
import android.support.v7.widget.AppCompatRadioButton;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.CompoundButton;
public class ColorRadioButton extends AppCompatRadioButton implements CompoundButton.OnCheckedChangeListener {
    private  int mNormalColor;
    private  int mCheckColor;
    private  int mNormalBGColor;
    private  int mCheckBGColor;
    private  int mCheckId;
    private int imageWidth;
    private int imageWidth_padding;
    boolean  mOnlyCheckShow=false;
    private String text="";
    public ColorRadioButton(Context context) {
        super(context);

    }
    public ColorRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
       initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ColorRadioButton);
        mNormalColor = typedArray.getColor(R.styleable.ColorRadioButton_ColorRadioButton_NormalColor, Color.WHITE);
        mCheckColor = typedArray.getColor(R.styleable.ColorRadioButton_ColorRadioButton_CheckColor, Color.YELLOW);
        mNormalBGColor = typedArray.getColor(R.styleable.ColorRadioButton_ColorRadioButton_NormalBGColor, Color.TRANSPARENT);
        mCheckBGColor = typedArray.getColor(R.styleable.ColorRadioButton_ColorRadioButton_CheckBGColor, Color.TRANSPARENT);
        text = typedArray.getString(R.styleable.ColorRadioButton_ColorRadioButton_Text);
        mOnlyCheckShow = typedArray.getBoolean(R.styleable.ColorRadioButton_ColorRadioButton_OnlyCheckShow,false);
        this.setBackground(null);

            setButtonDrawable(null);


        if (this.isChecked()){
            m_back_color=mCheckBGColor;
            this.setBackgroundColor(mCheckBGColor);
            this.setTextColor(mCheckColor);
        }else{
            m_back_color=mNormalBGColor;
            this.setBackgroundColor(mNormalBGColor);
            this.setTextColor(mNormalColor);
        }

        mCheckId = typedArray.getResourceId(R.styleable.ColorRadioButton_ColorRadioButton_CheckBackground, R.mipmap.ic_launcher);
        imageWidth=dip2px(context,35);


        imageWidth_padding=dip2px(context,12);
        setOnCheckedChangeListener(this);
    }

    public ColorRadioButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
       initView(context, attrs);
      //  setGravity(Gravity.LEFT);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (b){
            m_back_color=mCheckBGColor;
            setTextColor(mCheckColor);
            this.setBackgroundColor(mCheckBGColor);
        }else{
            m_back_color=mNormalBGColor;
            setTextColor(mNormalColor);
            this.setBackgroundColor(mNormalBGColor);
        }
    }
    Paint m_background_paint = new Paint();
    int   m_back_color = Color.WHITE;

    Paint m_text_paint = new Paint();
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int image_x=(int)(0.20d*this.getWidth());
        if (m_back_color!=Color.TRANSPARENT){
            m_background_paint.setColor(m_back_color);
            m_background_paint.setStyle(Paint.Style.FILL);//设置空心
            Rect rect=new Rect(0,0,this.getWidth(),this.getHeight());
            canvas.drawRect(rect, m_background_paint);
        }


        if (text!=null&&(!text.trim().equals(""))){

            String  test_string=text;
            m_text_paint.setTextAlign(Paint.Align.LEFT);
            Rect text_bounds = new Rect();
            m_text_paint.setTextSize(getTextSize());
            if (this.isChecked()){
                m_text_paint.setColor(mCheckColor);
            }else{
                m_text_paint.setColor(mNormalColor);
            }
            m_text_paint.getTextBounds(test_string, 0, test_string.length(), text_bounds);
            Paint.FontMetricsInt fontMetrics = m_text_paint.getFontMetricsInt();
            int baseline = (getMeasuredHeight() - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;
            if (mCheckId== R.mipmap.ic_launcher){

                canvas.drawText(test_string,getMeasuredWidth() / 2 - text_bounds.width() / 2, baseline, m_text_paint);
            }else{
                canvas.drawText(test_string,image_x+imageWidth+10, baseline, m_text_paint);
            }


        }

        if (mCheckId!=R.mipmap.ic_launcher){
            if (mOnlyCheckShow==true&&this.isChecked()==false){
                return;
            }
            canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG));
            m_text_paint.setAntiAlias(true);
            Bitmap draw_target_bitmap = null;
            draw_target_bitmap=     BitmapFactory.decodeResource(getResources(), mCheckId);
            Rect  draw_bitmap_rect = new Rect(image_x,(this.getHeight()-imageWidth)/2,
                    image_x+imageWidth,(this.getHeight()-imageWidth)/2+imageWidth);
            if (draw_target_bitmap!=null){
                canvas.drawBitmap(draw_target_bitmap,null,draw_bitmap_rect, m_text_paint);
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

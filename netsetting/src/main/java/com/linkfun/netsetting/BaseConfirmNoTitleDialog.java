package com.linkfun.netsetting;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.zhy.android.percent.support.PercentLayoutHelper;
import com.zhy.android.percent.support.PercentRelativeLayout;


/**
 * author：Administrator on 2017/4/11 15:43
 * description:文件说明
 * version:版本
 */
public class BaseConfirmNoTitleDialog extends Dialog implements View.OnClickListener{
    private final TextView ok;
    private final TextView cancel;

    private final TextView tvContent;
    private final TextView tvContentTwe;
    private  PercentRelativeLayout rlHeadLayout;
    private View conentView;


    public BaseConfirmNoTitleDialog(@NonNull Context context, String contenyText,int resId, OnConfirmClick onConfirmClick) {
        super(context, R.style.CommonBottomDialogStyle);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        conentView = inflater.inflate(R.layout.dialog_base_confirm_no_title, null);

        this.setContentView(conentView);
        setCancelable(false);//点击外部不可dismiss
        setCanceledOnTouchOutside(false);
        Window window = this.getWindow();
        window.setGravity(Gravity.CENTER);
        ok = (TextView)conentView.findViewById(R.id.btn_ok);
        cancel = (TextView)conentView.findViewById(R.id.btn_cancel);

        tvContent = (TextView)conentView.findViewById(R.id.tv_content);
        tvContentTwe = (TextView)conentView.findViewById(R.id.tv_content_twe);
        rlHeadLayout = (PercentRelativeLayout)conentView.findViewById(R.id.rl_head);
        rlHeadLayout.setBackgroundResource(resId);
        ok.setOnClickListener(this);
        cancel.setOnClickListener(this);
        if (contenyText.contains("#")){
            String[] splitText = contenyText.split("#");

            tvContent.setText(splitText[0]);
            tvContentTwe.setText(splitText[1]);
            tvContentTwe.setVisibility(View.VISIBLE);
        }else{
            tvContentTwe.setVisibility(View.GONE);
            tvContent.setText(contenyText);
        }

        this.onConfirmClick = onConfirmClick;
        this.show();


    }

    public void setOkText(String  okText){
        ok.setText(okText);

    }
    private static BaseConfirmNoTitleDialog instance;
    private static byte[] lock = new byte[0];


    public static BaseConfirmNoTitleDialog newInstance(@NonNull Context context, String contenText,int resId, OnConfirmClick onConfirmClick) {
        synchronized (lock) {
            if (instance == null) {
                instance = new BaseConfirmNoTitleDialog(context, contenText,resId, onConfirmClick);
            } else {
                instance.dismiss();
                instance = null;
                instance = new BaseConfirmNoTitleDialog(context, contenText, resId,onConfirmClick);
            }
        }
        return instance;
    }



    public OnConfirmClick onConfirmClick;

    public void setOnConfirmClick(OnConfirmClick onConfirmClick) {
        this.onConfirmClick = onConfirmClick;
    }

    @Override
    public void onClick(View view) {

        int id = view.getId();
        if (id==ok.getId()){
            if (onConfirmClick !=null){
                onConfirmClick.confirm();

            }

            dismiss();

        }
        if(id==cancel.getId()){
            if (onConfirmClick !=null){
                onConfirmClick.cancel();

            }
            dismiss();
        }
    }

    /**
     * 点击回调接口
     */
    public interface OnConfirmClick {
        void confirm();
        void cancel();
    }

    @Override
    public void dismiss() {
        super.dismiss();

    }
}

package com.linkfun.netsetting;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.zhy.android.percent.support.PercentRelativeLayout;

import java.util.List;


/**
 * author：Administrator on 2017/4/11 15:43
 * description:文件说明
 * version:版本
 */
public class PasswordInputDialog extends Dialog {

    private final PasswordCircleLayout mPasswordCircleLayout;
    private final ImageView ivDelete;
    private final Button btnConfirm;
    private final ImageView ivClose;
    private final TextView tvError;
    private int[] mButtonIds = new int[]{R.id.btn_number0, R.id.btn_number1, R.id.btn_number2, R.id.btn_number3,
            R.id.btn_number4, R.id.btn_number5, R.id.btn_number6, R.id.btn_number7, R.id.btn_number8, R.id.btn_number9};
    private  PercentRelativeLayout rlHeadLayout;
    private View conentView;
    public Context mContext;
    public PasswordInputDialog(@NonNull Context context,final OnConfirmClick onConfirmClick) {
        super(context, R.style.CommonBottomDialogStyle);
        mContext=context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        conentView = inflater.inflate(R.layout.dialog_password_input, null);
        this.setContentView(conentView);
        setCancelable(false);//点击外部不可dismiss
        setCanceledOnTouchOutside(false);
        Window window = this.getWindow();
        window.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(params);
        mPasswordCircleLayout = (PasswordCircleLayout)conentView.findViewById(R.id.tv_password);
        for (int mButtonId : mButtonIds) {
            conentView.findViewById(mButtonId).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Button button = (Button) view;
                    int clickNumber = Integer.parseInt(button.getText().toString());
                    mPasswordCircleLayout.addPassword(clickNumber);
                }
            });
        }

        ivDelete = (ImageView)conentView.findViewById(R.id.iv_delete);
        ivClose = (ImageView)conentView.findViewById(R.id.iv_close);
        btnConfirm = (Button)conentView.findViewById(R.id.btn_confirm);
        tvError = (TextView)conentView.findViewById(R.id.tv_error);
        ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPasswordCircleLayout.removePassword();
            }
        });
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Integer> passwordList = mPasswordCircleLayout.getPasswordList();
                if (passwordList.size()==6){
                    if (onConfirmClick!=null){
                        String textList="";
                        for (Integer integer : passwordList) {
                            textList+=(integer+"");
                        }
                        int passwordNumber = Integer.parseInt(textList);
                        if (passwordNumber==0){
                            onConfirmClick.confirm(passwordNumber);
                            dismiss();
                        }else{
                            tvError.setText("密码错误，请重新输入");
                        }

                    }
                    return;
                }
                if (passwordList.size()<6){
                    tvError.setText("输入密码长度不足,请输入六位数密码.");

                }
            }
        });
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onConfirmClick!=null){
                    onConfirmClick.cancel();
                }
                dismiss();
            }
        });
        this.onConfirmClick = onConfirmClick;
      this.show();

    }

    private static PasswordInputDialog instance;
    private static byte[] lock = new byte[0];


    public static PasswordInputDialog newInstance(@NonNull Context context, OnConfirmClick onConfirmClick) {
        synchronized (lock) {
            if (instance == null) {
                    instance = new PasswordInputDialog(context,  onConfirmClick);
            } else {
                instance.dismiss();
                instance = null;
                instance = new PasswordInputDialog(context, onConfirmClick);
        }
    }
        return instance;
    }



    public OnConfirmClick onConfirmClick;

    public void setOnConfirmClick(OnConfirmClick onConfirmClick) {
        this.onConfirmClick = onConfirmClick;
    }


    /**
     * 点击回调接口
     */
    public interface OnConfirmClick {
        void confirm(int  passwordList);
        void cancel();
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }
}

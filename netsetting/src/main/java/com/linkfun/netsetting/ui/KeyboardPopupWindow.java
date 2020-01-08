package com.linkfun.netsetting.ui;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.linkfun.netsetting.R;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class KeyboardPopupWindow extends PopupWindow {
    private static final String TAG = "KeyboardPopupWindow";
    private Context context;
    private View anchorView;
    private View parentView;
    private EditText editText;
    private boolean isRandomSort = false;//数字是否随机排序
    private List<Integer> list = new ArrayList<>();
    private int[] commonButtonIds = new int[]{R.id.button00, R.id.button01, R.id.button02, R.id.button03,
            R.id.button04, R.id.button05, R.id.button06, R.id.button07, R.id.button08, R.id.button09};

    private static KeyboardPopupWindow instance;
    private static byte[] lock = new byte[0];


    public static KeyboardPopupWindow newInstance(Context context, View anchorView, EditText editText) {
        synchronized (lock) {
            if (instance == null) {
                instance = new KeyboardPopupWindow(context, anchorView,editText ,false);
            } else {
                instance.dismiss();
                instance = null;
                instance = new KeyboardPopupWindow(context, anchorView,editText ,false);
            }
        }
        return instance;
    }


    /**
     * @param context
     * @param anchorView
     * @param editText
     * @param isRandomSort 数字是否随机排序
     */
    public KeyboardPopupWindow(Context context, View anchorView, EditText editText, boolean isRandomSort) {
        this.context = context;
        this.anchorView = anchorView;
        this.editText = editText;
        this.isRandomSort = isRandomSort;
        if (context == null || anchorView == null) {
            return;
        }
        initConfig();
        initView();
    }
   public static  void  KeyBack(){
        if (instance!=null){
            instance.dismiss();
            instance=null;
        }
   }

    private void initConfig() {
        setOutsideTouchable(false);
        setFocusable(false);
        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        forbidDefaultSoftKeyboard();
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (instance != null) {
                    instance.show();
                }
            }
        });
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
               // Log.d(TAG, "数字输入框是否有焦点——>" + hasFocus);

                if (instance != null && (editText.getVisibility()==View.VISIBLE)) {//很重要，Unable to add window -- token null is not valid; is your activity running?
                    instance.refreshKeyboardOutSideTouchable(!hasFocus);// 需要等待页面创建完成后焦点变化才去显示自定义键盘
                }

                if (hasFocus) {//隐藏系统软键盘
                    InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                }

            }
        });
    }

    /**
     * 禁止系统默认的软键盘弹出
     */
    private void forbidDefaultSoftKeyboard() {
        if (editText == null) {
            return;
        }
        if (android.os.Build.VERSION.SDK_INT > 10) {//4.0以上，使用反射的方式禁止系统自带的软键盘弹出
            try {
                Class<EditText> cls = EditText.class;
                Method setShowSoftInputOnFocus;
                setShowSoftInputOnFocus = cls.getMethod("setShowSoftInputOnFocus", boolean.class);
                setShowSoftInputOnFocus.setAccessible(true);
                setShowSoftInputOnFocus.invoke(editText, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 刷新自定义的popupwindow是否outside可触摸反应：如果是不可触摸的，则显示该软键盘view
     *
     * @param isTouchable
     */
    public void refreshKeyboardOutSideTouchable(boolean isTouchable) {
        setOutsideTouchable(isTouchable);
        if (!isTouchable) {

            show();
        } else {

            dismiss();
        }
    }

    private void initView() {
        parentView = LayoutInflater.from(context).inflate(R.layout.keyboadview, null);
        initKeyboardView(parentView);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setContentView(parentView);
    }

    private void initKeyboardView(View view) {
        LinearLayout dropdownLl = view.findViewById(R.id.dropdownLl);
        dropdownLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        //①给数字键设置点击监听
        for (int i = 0; i < commonButtonIds.length; i++) {
            final Button button = view.findViewById(commonButtonIds[i]);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int curSelection = editText.getSelectionStart();
                    int length = editText.getText().toString().length();
                    if (curSelection < length) {
                        String content = editText.getText().toString();
                        editText.setText(content.substring(0, curSelection) + button.getText() + content.subSequence(curSelection, length));
                        editText.setSelection(curSelection + 1);
                    } else {
                        editText.setText(editText.getText().toString() + button.getText());
                        editText.setSelection(editText.getText().toString().length());
                    }
                }
            });
        }

        //②给小数点按键设置点击监听
        view.findViewById(R.id.buttonDot).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int curSelection = editText.getSelectionStart();
                int length = editText.getText().toString().length();
                if (curSelection < length) {
                    String content = editText.getText().toString();
                    editText.setText(content.substring(0, curSelection) + "." + content.subSequence(curSelection, length));
                    editText.setSelection(curSelection + 1);
                } else {
                    editText.setText(editText.getText().toString() + ".");
                    editText.setSelection(editText.getText().toString().length());
                }
            }
        });

        //③给叉按键设置点击监听
        view.findViewById(R.id.buttonCross).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int length = editText.getText().toString().length();
                int curSelection = editText.getSelectionStart();
                if (length > 0 && curSelection > 0 && curSelection <= length) {
                    String content = editText.getText().toString();
                    editText.setText(content.substring(0, curSelection - 1) + content.subSequence(curSelection, length));
                    editText.setSelection(curSelection - 1);
                }
            }
        });
    }


    public void show() {
        if (!isShowing() && anchorView != null) {
            doRandomSortOp();
            this.showAtLocation(anchorView, Gravity.BOTTOM, 0, 0);
        }
    }

    /**
     * 随机分布数字
     */
    private void doRandomSortOp() {
        if (parentView == null) {
            return;
        }
        if (!isRandomSort) {
            for (int i = 0; i < commonButtonIds.length; i++) {
                final Button button = parentView.findViewById(commonButtonIds[i]);
                button.setText("" + i);
            }
        } else {
            list.clear();
            Random ran = new Random();
            while (list.size() < commonButtonIds.length) {
                int n = ran.nextInt(commonButtonIds.length);
                if (!list.contains(n))
                    list.add(n);//如果n不包涵在list中，才添加
            }
            for (int i = 0; i < commonButtonIds.length; i++) {
                final Button button = parentView.findViewById(commonButtonIds[i]);
                button.setText("" + list.get(i));
            }
        }
    }


    public void refreshViewAndShow(Context context, View anchorView, EditText editText) {
        this.context = context;
        this.anchorView = anchorView;
        this.editText = editText;
        if (context == null || anchorView == null) {
            return;
        }
        show();
    }

    public boolean isRandomSort() {
        return isRandomSort;
    }

    public void setRandomSort(boolean randomSort) {
        isRandomSort = randomSort;
    }

    public void releaseResources() {
        this.dismiss();
        context = null;
        anchorView = null;
        if (list != null) {
            list.clear();
            list = null;
        }
    }

}

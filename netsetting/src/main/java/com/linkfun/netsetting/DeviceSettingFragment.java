package com.linkfun.netsetting;

import android.content.DialogInterface;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.linkfun.netsetting.style_layout.StyleChangeEvent;
import com.linkfun.netsetting.utils.DataCleanManager;
import com.linkfun.netsetting.utils.SQLUtils;
import com.ys.rkapi.MyManager;

import org.greenrobot.eventbus.EventBus;

import ch.ielse.view.SwitchView;

public class DeviceSettingFragment extends Fragment {

    private SwitchView switchRootPassword;
    private EditText etRootPassword;
    private EditText etRebootTime;
    private RadioGroup rtnGroup;
    private RadioButton rtnStyle1;
    private RadioButton rtnStyle2;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_device_setting, container, false);
        switchRootPassword = (SwitchView) view.findViewById(R.id.switch_password);
        etRootPassword = (EditText) view.findViewById(R.id.et_root_password);
        etRebootTime= (EditText) view.findViewById(R.id.et_reboot_time);
        rtnStyle1= (RadioButton) view.findViewById(R.id.rtn_style1);
        rtnStyle2= (RadioButton) view.findViewById(R.id.rtn_style2);
        rtnGroup= (RadioGroup) view.findViewById(R.id.rtn_group);
        int styleNumber = SQLUtils.getStyleNumber(getContext());
        if (styleNumber==1){
            rtnStyle1.setChecked(true);
            rtnStyle2.setChecked(false);
        }else{
            rtnStyle1.setChecked(false);
            rtnStyle2.setChecked(true);
        }
        rtnGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (rtnStyle1.isChecked()){
                    EventBus.getDefault().post(new StyleChangeEvent(1));
                    SQLUtils.setStyleNumber(getContext(),1);
                }     if (rtnStyle2.isChecked()){
                    EventBus.getDefault().post(new StyleChangeEvent(2));
                    SQLUtils.setStyleNumber(getContext(),2);
                }
            }
        });
        view.findViewById(R.id.ll_reboot).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                BaseConfirmNoTitleDialog.newInstance(getContext(), "确定重启吗?", R.mipmap.bg_setting_reboot, new BaseConfirmNoTitleDialog.OnConfirmClick() {
                    @Override
                    public void confirm() {
                        MyManager.getInstance(getContext()).reboot();
                    }

                    @Override
                    public void cancel() {

                    }
                });

            }
        });
        view.findViewById(R.id.ll_clear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BaseConfirmNoTitleDialog.newInstance(getContext(), "确定重置吗?#该操作会清空所有数据", R.mipmap.bg_setting_reboot, new BaseConfirmNoTitleDialog.OnConfirmClick() {
                    @Override
                    public void confirm() {
                        DataCleanManager.cleanApplicationData(getContext());
                        MyManager.getInstance(getContext()).reboot();
                    }

                    @Override
                    public void cancel() {

                    }
                });
            }
        });
        String rootPassword = SQLUtils.getRootPassword(getContext());
        if (!rootPassword.trim().equals("")) {
            etRootPassword.setText(rootPassword);
        }
        switchRootPassword.setOpened(SQLUtils.getRootPasswordEnable(getContext()));
        initSwitch();
        switchRootPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initSwitch();
            }
        });
        KeyboardPopupWindow.newInstance(getContext(), getActivity().getWindow().getDecorView(), etRootPassword);
        String rebootTime = SQLUtils.getRebootTime(getContext());
        if (!rebootTime.trim().equals("")){
            etRebootTime.setText(rebootTime);
        }
        return view;

    }

    @Override
    public void onDestroy() {
        SQLUtils.setRootPasswordEnable(getContext(), switchRootPassword.isOpened());
        SQLUtils.setRootPassword(getContext(), etRootPassword.getText().toString().trim());
        SQLUtils.setRebootTime(getContext(),etRebootTime.getText().toString().trim());
        KeyboardPopupWindow.KeyBack();
        super.onDestroy();
    }

    private void initSwitch() {
        if (switchRootPassword.isOpened()) {
            etRootPassword.getPaint().setFlags(Paint.ANTI_ALIAS_FLAG);
        } else {
            etRootPassword.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

        }
        etRootPassword.setText(etRootPassword.getText().toString());
        etRootPassword.setSelection(etRootPassword.getText().toString().length());
        etRootPassword.invalidate();

    }
}

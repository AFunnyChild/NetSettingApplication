package com.linkfun.netsetting;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.linkfun.netsetting.utils.SQLUtils;
import com.ys.rkapi.MyManager;

public class InternetSettingFragment extends Fragment {

    private EditText etServerIp;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_internet_setting, container, false);
        etServerIp = (EditText) view.findViewById(R.id.et_server_ip);
        etServerIp.setText(SQLUtils.getServerPath(getContext()));
        TextView tvSn = (TextView) view.findViewById(R.id.tv_sn);
        tvSn.setText(MyManager.getInstance(getContext()).getEthMacAddress());
        return view;

    }

    @Override
    public void onDestroy() {
        SQLUtils.setServerPath(getContext(),etServerIp.getText().toString());
        super.onDestroy();
    }
}

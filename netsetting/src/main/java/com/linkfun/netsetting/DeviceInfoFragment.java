package com.linkfun.netsetting;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.linkfun.netsetting.utils.WifiAdminUtils;
import com.ys.rkapi.MyManager;

import ch.ielse.view.SwitchView;

public class DeviceInfoFragment extends Fragment {

  private SwitchView switchRootPassword;
  private TextView tvIp;
  private TextView tvSn;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

    View view = inflater.inflate(R.layout.fragment_device_param, container, false);

    tvIp = (TextView) view.findViewById(R.id.tv_ip);
    tvSn = (TextView) view.findViewById(R.id.tv_sn);
   tvIp.setText(WifiAdminUtils.getLocalIpAddress(getContext()));
   tvSn.setText(MyManager.getInstance(getContext()).getEthMacAddress());

    return view;

  }

  @Override
  public void onDestroy() {

    super.onDestroy();
  }


}

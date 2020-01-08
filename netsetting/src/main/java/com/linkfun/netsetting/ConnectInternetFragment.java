package com.linkfun.netsetting;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.linkfun.netsetting.ui.WifiLayout;

import ch.ielse.view.SwitchView;

public class ConnectInternetFragment extends Fragment {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_connect_internet, container, false);
        final SwitchView wifiSwitchView = view.findViewById(R.id.switch_wifi);
        final WifiLayout wifiLayout = view.findViewById(R.id.wifi_layout);
        final SwitchView etaiSwitchView = view.findViewById(R.id.switch_etai);
        wifiSwitchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               wifiLayout.setVisibility(wifiSwitchView.isOpened()?View.VISIBLE:View.INVISIBLE);

            }
        });
      //  etaiSwitchView.setEnabled(false);
        return view;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}

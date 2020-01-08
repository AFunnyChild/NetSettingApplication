package com.linkfun.netsetting;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.linkfun.netsetting.style_layout.StyleChangeEvent;

import org.greenrobot.eventbus.EventBus;

public class NetSettingActivity extends FragmentActivity {

    private String[] mTitles = new String[]{"网络设置", "服务器参数", "设备类型","设备参数"};
    private Fragment[] fragments;
    private RadioGroup rtnGroup;
    private ViewPager viewPager;
    private RadioButton rtnInternet;
    private RadioButton rtnServerParam;
    private RadioButton rtnDeviceSetting;
    private RadioButton rtnDeviceParam;

    @Override
    protected void onCreate(@android.support.annotation.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
       setContentView(R.layout.activity_net_setting);

        rtnGroup = findViewById(R.id.rtn_group);
        viewPager = findViewById(R.id.view_pager);
        rtnInternet = findViewById(R.id.rtn_internet);
        rtnServerParam = findViewById(R.id.rtn_server_param);
        rtnDeviceSetting = findViewById(R.id.rtn_device_setting);
        rtnDeviceParam = findViewById(R.id.rtn_device_param);
        findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        initViewPage();
      //  rtnInternet.setChecked(true);
    }
    private void initViewPage() {
        fragments = new Fragment[]{new ConnectInternetFragment(), new InternetSettingFragment()
                , new DeviceSettingFragment(),new DeviceInfoFragment()};
      viewPager.setAdapter(new FragmentPageAdapter(getSupportFragmentManager(),fragments));
      rtnGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (rtnInternet.isChecked()) {
                    viewPager.setCurrentItem(0, false);
                }
                if (rtnServerParam.isChecked()) {
                  viewPager.setCurrentItem(1, false);
                }
                if (rtnDeviceSetting.isChecked()) {
                 viewPager.setCurrentItem(2, false);
                }
                if (rtnDeviceParam.isChecked()) {

                 viewPager.setCurrentItem(3, false);
                }
            }
        });
    }
    public class FragmentPageAdapter extends FragmentPagerAdapter {
        private Fragment[]  list;
        public FragmentPageAdapter(FragmentManager fm, Fragment[]  list) {
            super(fm);
            this.list=list;
        }

        @Override
        public Fragment getItem(int arg0) {
            return list[arg0];
        }

        @Override
        public int getCount() {
            return list.length;
        }
    }

}

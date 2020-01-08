package com.linkfun.netsetting.ui;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.linkfun.netsetting.R;
import com.linkfun.netsetting.ui.adapter.MyListViewAdapter;
import com.linkfun.netsetting.ui.api.OnNetworkChangeListener;
import com.linkfun.netsetting.ui.dialog.WifiConnDialog;
import com.linkfun.netsetting.ui.dialog.WifiStatusDialog;
import com.linkfun.netsetting.utils.WifiAdminUtils;


import java.util.ArrayList;
import java.util.List;



public class WifiLayout extends RelativeLayout {


    private static final int REFRESH_CONN = 100;
    // Wifi管理类
    private WifiAdminUtils mWifiAdmin;
    // 扫描结果列表
    private List<ScanResult> list = new ArrayList<ScanResult>();


    private MyListViewAdapter mAdapter;
    //下标
    private int mPosition;

    private WifiReceiver mReceiver=null;

    private OnNetworkChangeListener mOnNetworkChangeListener = new OnNetworkChangeListener() {

        @Override
        public void onNetWorkDisConnect() {
            getWifiListInfo();
            mAdapter.setDatas(list);
            mAdapter.notifyDataSetChanged();
        }

        @Override
        public void onNetWorkConnect() {
            getWifiListInfo();
            mAdapter.setDatas(list);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            mAdapter.notifyDataSetChanged();
        }
    };



    private ListView listView;

    public WifiLayout(Context context) {
        super(context);
        setWillNotDraw(false);
    }

    public WifiLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);
        initView(context, attrs);
    }
    public WifiLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setWillNotDraw(false);
        initView(context, attrs);
    }
    private void initView(Context context, AttributeSet attrs) {

        listView = new ListView(context);
        listView.setDividerHeight(0);
        setGravity(Gravity.TOP);
        addView(listView);
        initPermission();
        registerReceiver();
        initData();
        setListener();
        refreshWifiStatusOnTime();
    }
    private void registerReceiver() {
        mReceiver = new WifiReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        filter.addAction(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION);
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        getContext().registerReceiver(mReceiver, filter);
    }
    /**
     * 取消广播
     */
    private void unregisterReceiver() {
        if (mReceiver != null) {
            getContext().unregisterReceiver(mReceiver);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        unregisterReceiver();
    }

    private void setListener() {


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos,
                                    long id) {
                mPosition = pos ;
                ScanResult scanResult = list.get(mPosition);
                String desc = "";
                String descOri = scanResult.capabilities;
                if (descOri.toUpperCase().contains("WPA-PSK")) {
                    desc = "WPA";
                }
                if (descOri.toUpperCase().contains("WPA2-PSK")) {
                    desc = "WPA2";
                }
                if (descOri.toUpperCase().contains("WPA-PSK")
                        && descOri.toUpperCase().contains("WPA2-PSK")) {
                    desc = "WPA/WPA2";
                }

                if (desc.equals("")) {
                    isConnectSelf(scanResult);
                    return;
                }
                isConnect(scanResult);
            }

            /**
             * 有密码验证连接
             * @param scanResult
             */
            private void isConnect(ScanResult scanResult) {
                if (mWifiAdmin.isConnect(scanResult)) {
                    // 已连接，显示连接状态对话框
                    WifiStatusDialog mStatusDialog = new WifiStatusDialog(
                            getContext(), R.style.defaultDialogStyle,
                            scanResult, mOnNetworkChangeListener);
                    mStatusDialog.show();
                } else {
                    // 未连接显示连接输入对话框
                    WifiConnDialog mDialog = new WifiConnDialog(
                            getContext(), R.style.defaultDialogStyle, listView, mPosition, mAdapter,
                            scanResult, list, mOnNetworkChangeListener);
                    mDialog.show();
                }
            }

            /**
             * 无密码直连
             * @param scanResult
             */
            private void isConnectSelf(ScanResult scanResult) {
                if (mWifiAdmin.isConnect(scanResult)) {
                    // 已连接，显示连接状态对话框
                    WifiStatusDialog mStatusDialog = new WifiStatusDialog(
                            getContext(), R.style.defaultDialogStyle,
                            scanResult, mOnNetworkChangeListener);
                    mStatusDialog.show();
                } else {
                    boolean iswifi = mWifiAdmin.connectSpecificAP(scanResult);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (iswifi) {
                        Toast.makeText(getContext(), "连接成功！",Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "连接失败！", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    /**
     * 初始化数据
     */
    private void initData() {
        mWifiAdmin = new WifiAdminUtils(getContext());
        // 获得Wifi列表信息
        getWifiListInfo();
    }
    private void initPermission() {
//        RxPermissions.getInstance(getContext())
//                .request(Manifest.permission.ACCESS_COARSE_LOCATION)//这里填写所需要的权限
//                .subscribe(new Action1<Boolean>() {
//                    @Override
//                    public void call(Boolean aBoolean) {
//                        if (aBoolean) {//true表示获取权限成功（注意这里在android6.0以下默认为true）
//                            Log.i("permissions", Manifest.permission.READ_CALENDAR + "：" + "获取成功");
//                        } else {
//                            Log.i("permissions", Manifest.permission.READ_CALENDAR + "：" + "获取失败");
//                        }
//                    }
//                });
    }
    private class WifiReceiver extends BroadcastReceiver {
        protected static final String TAG = "MainActivity";
        //记录网络断开的状态
        private boolean isDisConnected = false;
        //记录正在连接的状态
        private boolean isConnecting = false;

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {// wifi连接上与否
                Log.d(TAG, "网络已经改变");
                NetworkInfo info = intent
                        .getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
                if (info.getState().equals(NetworkInfo.State.DISCONNECTED)) {
                    if (!isDisConnected) {
                        Log.d(TAG, "wifi已经断开");
                        isDisConnected = true;
                    }
                } else if (info.getState().equals(NetworkInfo.State.CONNECTING)) {
                    if (!isConnecting) {
                        Log.d(TAG, "正在连接...");
                        isConnecting = true;
                    }
                } else if (info.getState().equals(NetworkInfo.State.CONNECTED)) {
                    WifiManager wifiManager = (WifiManager) context
                            .getSystemService(Context.WIFI_SERVICE);
                    WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                    Log.d(TAG, "连接到网络：" + wifiInfo.getBSSID());
                }

            } else if (intent.getAction().equals(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION)) {
                int error = intent.getIntExtra(WifiManager.EXTRA_SUPPLICANT_ERROR,
                        0);
                switch (error) {

                    case WifiManager.ERROR_AUTHENTICATING:
                        Log.d(TAG, "密码认证错误Code为：" + error);
                        Toast.makeText(getContext(), "wifi密码认证错误！", Toast.LENGTH_SHORT).show();
                        break;

                    default:
                        break;
                }

            } else if (intent.getAction().equals(WifiManager.WIFI_STATE_CHANGED_ACTION)) {
                int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0);
                Log.e("H3c", "wifiState" + wifiState);
                switch (wifiState) {
                    case WifiManager.WIFI_STATE_ENABLING:
                        Log.d(TAG, "wifi正在启用");
                        break;
                    case WifiManager.WIFI_STATE_ENABLED:
                        Log.d(TAG, "Wi-Fi已启用。");
                        break;

                }
            }
        }

    }

    private  class MyHandler extends Handler {


        @Override
        public void handleMessage(Message msg) {



            switch (msg.what) {
                case REFRESH_CONN:
                             getWifiListInfo();
                   mAdapter.setDatas(list);
                   mAdapter.notifyDataSetChanged();
                    break;

                default:
                    break;
            }

            super.handleMessage(msg);
        }
    }
    /**
     * 得到wifi的列表信息
     */
    private void getWifiListInfo() {

        mWifiAdmin.startScan();
        List<ScanResult> tmpList = mWifiAdmin.getWifiList();
        if (tmpList == null) {
            list.clear();
        } else {
            list.clear();
            list.addAll(tmpList);
        }
        if (mAdapter==null){

            mAdapter = new MyListViewAdapter(getContext(), list);
            listView.setAdapter(mAdapter);
        }


        mAdapter.setDatas(list);
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mAdapter.notifyDataSetChanged();
            }
        });

    }

    private Handler mHandler = new MyHandler();

    protected boolean isUpdate = true;
    /**
     * 定时刷新Wifi列表信息
     *
     * @author Xiho
     */
    private void refreshWifiStatusOnTime() {
        new Thread() {
            public void run() {
                while (isUpdate) {
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    mHandler.sendEmptyMessage(REFRESH_CONN);
                }
            }
        }.start();
    }

}

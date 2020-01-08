package com.linkfun.netsetting.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.linkfun.netsetting.style_layout.StyleChangeEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2018/5/5.
 */

public class SQLUtils {

    private static final String STYLE_NUMBER = "STYLE_NUMBER";
    private static final String SERVER_PATH = "SERVER_PATH";
    private static final String DEVICE_SN = "DEVICE_SN";
    private static final String ROOT_PASSWORD = "ROOT_PASSWORD";
    private static final String ROOT_FINGER= "ROOT_FINGER";
    private static final String REBOOT_TIME= "REBOOT_TIME";
    private static final String ROOT_PASSWORD_ENABLE= "ROOT_PASSWORD_ENABLE";


    public static SharedPreferences getSharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }
    public static String getServerPath(Context context){
        if (context == null)return "";
        SharedPreferences sp = getSharedPreferences(context);
        return sp.getString(SERVER_PATH, "");
    }
    public static void setServerPath(Context context, String password) {
        if (context == null)return;
        SharedPreferences sp = getSharedPreferences(context);
        sp.edit().putString(SERVER_PATH, password).apply();
    }
    public static String getDeviceSn(Context context){
        if (context == null)return "";
        SharedPreferences sp = getSharedPreferences(context);
        return sp.getString(DEVICE_SN, "");
    }
    public static void setDeviceSn(Context context, String password) {
        if (context == null)return;
        SharedPreferences sp = getSharedPreferences(context);
        sp.edit().putString(DEVICE_SN, password).apply();
    }
    public static String getRootPassword(Context context){
        if (context == null)return "";
        SharedPreferences sp = getSharedPreferences(context);
        return sp.getString(ROOT_PASSWORD, "");
    }
    public static void setRootPassword(Context context, String password) {
        if (context == null)return;
        SharedPreferences sp = getSharedPreferences(context);
        sp.edit().putString(ROOT_PASSWORD, password).apply();
    }
    public static String getRootFinger(Context context){
        if (context == null)return "";
        SharedPreferences sp = getSharedPreferences(context);
        return sp.getString(ROOT_FINGER, "");
    }
    public static void setRootFinger(Context context, String password) {
        if (context == null)return;
        SharedPreferences sp = getSharedPreferences(context);
        sp.edit().putString(ROOT_FINGER, password).apply();
    }
    public static String getRebootTime(Context context){
        if (context == null)return "";
        SharedPreferences sp = getSharedPreferences(context);
        return sp.getString(REBOOT_TIME, "");
    }
    public static void setRebootTime(Context context, String password) {
        if (context == null)return;
        SharedPreferences sp = getSharedPreferences(context);
        sp.edit().putString(REBOOT_TIME, password).apply();
        notifyRebootTimeChange(context);
    }
    public static boolean getRootPasswordEnable(Context context){
        if (context == null)return false;
        SharedPreferences sp = getSharedPreferences(context);
        return sp.getBoolean(ROOT_PASSWORD_ENABLE,true);
    }
    public static void setRootPasswordEnable(Context context, Boolean password) {
        if (context == null)return;
        SharedPreferences sp = getSharedPreferences(context);
        sp.edit().putBoolean(ROOT_PASSWORD_ENABLE, password).apply();
    }

    public static int getStyleNumber(Context context){
        if (context == null)return 1;
        SharedPreferences sp = getSharedPreferences(context);
        return sp.getInt(STYLE_NUMBER,1);
    }
    public static void setStyleNumber(Context context, int password) {
        if (context == null)return;
        SharedPreferences sp = getSharedPreferences(context);
        sp.edit().putInt(STYLE_NUMBER, password).apply();
        EventBus.getDefault().post(new StyleChangeEvent(password));
    }


   static   List<onRebootTimeChangeListener>  rebootTimeChangeListenerList=new ArrayList<>();
    public static void addOnRebootTimeChangeListener(onRebootTimeChangeListener  onRebootTimeChangeListener){
        if (onRebootTimeChangeListener!=null){
            rebootTimeChangeListenerList.add(onRebootTimeChangeListener);
        }
    }
    public static   void removeOnRebootTimeChangeListener(onRebootTimeChangeListener  onRebootTimeChangeListener){
        if (onRebootTimeChangeListener!=null){
            rebootTimeChangeListenerList.remove(onRebootTimeChangeListener);
        }
    }
    public static void notifyRebootTimeChange(Context  context){
        for (onRebootTimeChangeListener onRebootTimeChangeListener : rebootTimeChangeListenerList) {
            String rebootTime=getRebootTime(context);
            onRebootTimeChangeListener.onRebootTimeChange(rebootTime);
        }
    }
    public  interface  onRebootTimeChangeListener{
     void   onRebootTimeChange(String time);
    }
}

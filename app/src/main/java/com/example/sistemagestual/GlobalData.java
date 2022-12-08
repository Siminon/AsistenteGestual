package com.example.sistemagestual;

import android.app.Application;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

import java.net.Inet4Address;

public class GlobalData extends Application {
    private static GlobalData instance;
    private static boolean next;
    private static Integer level;
    public long startTime, elapseTime;
    public long waitTime = 999999999;
    private static View view;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        level = 0;
        startTime = 0;
        elapseTime = 0;
    }

    public static void setView(View view) {GlobalData.view = view;}

    public static View getView() {return GlobalData.view;}

    public static GlobalData getInstance() {
        return instance;
    }

    public GlobalData getContext() {
        return this;
    }

    public static void updateLevel(int level) {
        GlobalData.level = level;
    }

    public static Integer getLevel() {
        return GlobalData.level;
    }
}
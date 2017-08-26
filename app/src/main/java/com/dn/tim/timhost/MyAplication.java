package com.dn.tim.timhost;


import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.dn.tim.timhost.hook.AMSHookHelper;

public class MyAplication extends Application{
    private static Context sContext;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        sContext = base;

    }

//    @Override
//    public void onCreate() {
//        super.onCreate();
//        sContext = this;
//    }

    public static Context getContext() {
        return sContext;
    }
}

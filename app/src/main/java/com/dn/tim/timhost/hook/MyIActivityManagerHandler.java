package com.dn.tim.timhost.hook;


import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import android.content.ComponentName;
import android.content.Intent;
import android.util.Log;

import com.dn.tim.timhost.MyAplication;
import com.dn.tim.timhost.PoxyActivity;


public class MyIActivityManagerHandler implements InvocationHandler{


    private static final String TAG = "tim_IAMHandler";

    Object mBase;

    public MyIActivityManagerHandler(Object base) {
        mBase = base;
    }

    @Override
    public Object invoke(Object o, Method method, Object[] objects) throws Throwable {


        if ("startActivity".equals(method.getName())) {
            Log.d(TAG,"I will hock this method : "+method.toString());
            // 只拦截这个方法
            // 替换参数, 任你所为;甚至替换原始Activity启动别的Activity偷梁换柱
            // API 23:
            // public final Activity startActivityNow(Activity parent, String id,
            // Intent intent, ActivityInfo activityInfo, IBinder token, Bundle state,
            // Activity.NonConfigurationInstances lastNonConfigurationInstances) {

            // 找到参数里面的第一个Intent 对象
            Intent raw;
            int index = 0;
            for (int i =0; i< objects.length; i++) {
                if (objects[i] instanceof  Intent) {
                    index = i;
                    break;
                }
            }
            raw = (Intent) objects[index];
            Intent newIntent = new Intent();
            String stubPackage = MyAplication.getContext().getPackageName();
            // 这里我们把启动的Activity临时替换为 PoxyActivity
            ComponentName componentName = new ComponentName(stubPackage, PoxyActivity.class.getName());
            newIntent.setComponent(componentName);
            // 把我们原始要启动的TargetActivity先存起来
            newIntent.putExtra("oldIntent", raw);
            // 替换掉Intent, 达到欺骗AMS的目的
            objects[index] = newIntent;
        }
        return method.invoke(mBase,objects);
    }
}

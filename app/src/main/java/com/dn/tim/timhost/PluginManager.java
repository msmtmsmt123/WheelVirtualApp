package com.dn.tim.timhost;

import android.content.Context;
import android.content.res.AssetManager;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;

/**
 * Created by tim on 2017/5/31.
 * 下载插件 加载 卸载 增量更新 黑/白名单
 */

public class PluginManager {

    public static void loadPlugin(Context context) {
        InputStream is = null;
        FileOutputStream os = null;

        try {
            is = context.getAssets().open(Constants.PLUGIN_NAME);
            os = new FileOutputStream(Constants.FilePath);
            int len;
            byte[] buffer = new  byte[1024];
            while ((len = is.read(buffer))!= -1) {
                os.write(buffer, 0, len);
            }
            Toast.makeText(context, "plugin load ok!", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static AssetManager getPluginAssetManager(File apkFile) throws Exception {

        Class<?> forName = Class.forName("android.content.res.AssetManager");
        Method[] deClareMethods = forName.getDeclaredMethods();
        for (Method method : deClareMethods) {
            if (method.getName().equals("addAssetPath")) {

                AssetManager assetManager = AssetManager.class.newInstance();
                method.invoke(assetManager, apkFile.getAbsolutePath());
                return assetManager;
            }
        }
        return null;
    }

}

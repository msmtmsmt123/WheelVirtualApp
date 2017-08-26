package com.dn.tim.timhost;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.dn.tim.timhost.hook.AMSHookHelper;
import com.dn.tim.timhost.hook.classLoder.LoadedApkClassLoaderHookHelper;
import com.dn.tim.timhost.hook.classLoder.Utils;

import java.io.File;
import java.lang.reflect.Field;

import dalvik.system.DexClassLoader;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{


    private ImageView imageSource;
    private ImageView imageCloud;
    private Button button;
    private final static String SRC_TAG = "srcTag";
    private final static String CLOUD_TAG = "cloud";
    private final static String Button_TAG = "button";
    private final static String TAG = "MainAcitivty_Tim";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        imageSource = (ImageView) findViewById(R.id.imageSource);
        imageCloud = (ImageView) findViewById(R.id.imageCloud);
        button = (Button) findViewById(R.id.imageButon);
        button.setTag(Button_TAG);
        imageSource.setTag(SRC_TAG);
        imageCloud.setTag(CLOUD_TAG);
        imageSource.setOnClickListener(this);
        imageCloud.setOnClickListener(this);
        button.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        if (v.getTag().equals(Button_TAG)) {
            Intent intent = new Intent();
      //      intent.setComponent(new ComponentName("com.dn.tim.plugin","com.dn.tim.plugin.PluginActivity"));
            intent.setComponent(new ComponentName("viewgroup.selfview.com.wheelmenulayout","viewgroup.selfview.com.wheelmenulayout.MainActivity"));
            startActivity(intent);
        } else if (v.getTag().equals(SRC_TAG)) {
            hanldeAnim(v);
        } else {

            //如果点击的是插件  1. 加载插件APK
            File apkFile = new File(Constants.FilePath);
            if (apkFile.exists()) {
                //加载插件里面的帧动画
                Drawable backGorud = v.getBackground();
                if (backGorud instanceof  AnimationDrawable) {
                    hanldeAnim(v);
                } else {
//                    ; 放射   代码注入攻击 优化
                    DexClassLoader classLoader = new DexClassLoader(apkFile.getAbsolutePath(),
                            this.getDir(Constants.PLUGIN_NAME, Context.MODE_PRIVATE).getAbsolutePath(), null,
                            getClassLoader());

                    Class<?> loadClass = null;
//                    Log.d("tim","xxx");
                    try {

                        loadClass = classLoader.loadClass(Constants.PackageName + ".R$drawable");
                        Field[] declaredFilelds = loadClass.getDeclaredFields();
                        for (Field field : declaredFilelds) {
                            if (field.getName().equals(CLOUD_TAG)) {
                                int animId = field.getInt(R.drawable.class); //2130837594
                                //Drawable drawable = this.getResources().getDrawable(animId);
                               // Resources resources = this.getResources(); //内有乾坤
//                                AssetManager assetManager = new AssetManager();
                                AssetManager assetManager = PluginManager.getPluginAssetManager(apkFile);
                                Resources resources = new Resources(assetManager, this.getResources().getDisplayMetrics(),
                                        this.getResources().getConfiguration());
                                Drawable drawable = resources.getDrawable(animId);
                                v.setBackgroundDrawable(drawable);
//                                startActivity(new Intent(xxx));
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {
                //TODO 插件管理 1/3 业务逻辑
                PluginManager.loadPlugin(this);
            }
        }

    }
    private void hanldeAnim(View v) {
        AnimationDrawable animationDrawable = (AnimationDrawable) v.getBackground();
        if( animationDrawable != null) {
            if (!animationDrawable.isRunning()) {
                animationDrawable.start();
            } else {
                animationDrawable.stop();
            }
        }

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        try {
//            File dexFile = new File(Constants.FilePath);
//            File optDexFile = new File(Environment.getExternalStorageDirectory() + File.separator + "classes.dex");

            Utils.extractAssets(newBase, "Plugin.apk");
            LoadedApkClassLoaderHookHelper.hookLoadedApkInActivityThread(getFileStreamPath("Plugin.apk"));

            AMSHookHelper.hookActivityManagerNative();

            AMSHookHelper.hookActivityThreadHandler();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("tim","Hook error");
        }
    }
}

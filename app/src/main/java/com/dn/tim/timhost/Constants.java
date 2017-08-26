package com.dn.tim.timhost;

import android.os.Environment;

import java.io.File;

/**
 * Created by tim on 2017/5/31.
 */

public class Constants {
    public static final String PLUGIN_NAME = "Plugin.apk";
    public static final String FilePath = Environment.getExternalStorageDirectory() + File.separator + PLUGIN_NAME;
    public static final String PackageName = "com.dn.tim.plugin";
}

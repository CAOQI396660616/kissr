package com.dubu.download.engine.constants;

import android.content.Context;
import android.os.Build;

/**
 *
 * @date :2021/6/9 16:59
 * @des :
 */
public class AndroidOptOS {
    public static boolean USE_SCOPED_STORAGE;
    public static void initConfig(Context context){
        //android11的适配版本 android11 adaptation
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            USE_SCOPED_STORAGE = true;
        }
    }
}

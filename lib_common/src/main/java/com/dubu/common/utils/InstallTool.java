package com.dubu.common.utils;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import androidx.core.content.FileProvider;

import com.dubu.common.base.BaseApp;
import com.dubu.common.constant.Tag2Common;

import java.io.File;
import java.lang.ref.WeakReference;

public class InstallTool {

    public static void installApkProcess(File apkFile) {
        if (Build.VERSION.SDK_INT >= 26) {
            boolean hasInstallPermission = BaseApp.instance.getPackageManager().canRequestPackageInstalls();
            if (!hasInstallPermission) {//没有安装未知来源的权限
                startInstallPermissionSettingActivity();
                //去设置之前，先暂存一下安装包，等设置完，载安装
                //CommonTemp.getInstance().setWaitingForInstallPermissionSettingApkFile(apkFile);
                return;
            }
        }
        installApk(apkFile);
    }

    public static void startInstallPermissionSettingActivity() {
        Uri packageURI = Uri.parse("package:" + BaseApp.instance.getPackageName());
        //注意这个是8.0新API
        Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, packageURI);
        WeakReference<Activity> topActivity = BaseApp.Companion.getTopActivity();
        Activity activity = topActivity.get();
        activity.startActivityForResult(intent, 6000);
    }

    public static void installApk(File apkFile) {
        if (apkFile == null || !apkFile.exists()) {
            return;
        }
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {//Android7.0


            String authority = "com.dabai.kiss.chat.fileProvider";
            Uri contentUri = FileProvider.getUriForFile( BaseApp.instance, authority, apkFile);

            HiLog.e(Tag2Common.TAG_12300, "installApk authority" + authority);
            HiLog.e(Tag2Common.TAG_12300, "installApk apkFile getAbsolutePath" + apkFile.getAbsolutePath());


            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            // 通过Intent安装APK文件
            intent.setDataAndType(Uri.parse("file://" + apkFile.toString()),
                    "application/vnd.android.package-archive");
        }
        if ( BaseApp.instance.getPackageManager().queryIntentActivities(intent, 0).size() > 0) {
            WeakReference<Activity> topActivity = BaseApp.Companion.getTopActivity();
            Activity activity = topActivity.get();
            activity.startActivity(intent);
            //安装完，清除缓存
           // CommonTemp.getInstance().setWaitingForInstallPermissionSettingApkFile(null);
        }
    }


/*    public static void installApk(File apkFile) {
        if (apkFile == null || !apkFile.exists()) return;

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); // 统一添加权限

        // 使用应用上下文获取包名
        String authority = BaseApp.instance.getPackageName() + ".fileprovider";

        Uri contentUri = FileProvider.getUriForFile(
                BaseApp.instance,
                authority,  // 使用动态生成的authority
                apkFile
        );
        intent.setDataAndType(contentUri, "application/vnd.android.package-archive");

        // 安全启动 Activity
        WeakReference<Activity> topActivity = BaseApp.Companion.getTopActivity();
        if (topActivity != null && topActivity.get() != null) {
            topActivity.get().startActivity(intent);
        } else {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            BaseApp.instance.startActivity(intent);
        }
    }*/

}


package com.dubu.common.utils;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageInstaller;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class PackageInstallerHelper {

    public static void installApk(Context context, File apkFile) {
        if (context == null || apkFile == null || !apkFile.exists()) {
            Log.e("PackageInstaller", "无效参数或APK文件不存在");
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Android 8.0+ 需要安装权限
            if (!context.getPackageManager().canRequestPackageInstalls()) {
                requestInstallPermission(context);
                return;
            }
        }

        try {
            PackageInstaller packageInstaller = context.getPackageManager().getPackageInstaller();
            PackageInstaller.SessionParams params = new PackageInstaller.SessionParams(
                    PackageInstaller.SessionParams.MODE_FULL_INSTALL
            );

            // 设置安装来源（仅适用于API 31+）
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                params.setRequireUserAction(PackageInstaller.SessionParams.USER_ACTION_NOT_REQUIRED);
            }

            int sessionId = packageInstaller.createSession(params);
            PackageInstaller.Session session = packageInstaller.openSession(sessionId);

            // 将APK文件写入会话
            try (InputStream in = new FileInputStream(apkFile);
                 OutputStream out = session.openWrite("apk", 0, apkFile.length())) {
                byte[] buffer = new byte[8192];
                int length;
                while ((length = in.read(buffer)) > 0) {
                    out.write(buffer, 0, length);
                }
                session.fsync(out);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            // 创建安装状态接收器
            IntentSender statusReceiver = createStatusReceiver(context, sessionId);

            // 提交安装
            session.commit(statusReceiver);
            session.close();
        } catch (IOException | SecurityException e) {
            Log.e("PackageInstaller", "安装失败: " + e.getMessage());
        }
    }

    private static void requestInstallPermission(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
            intent.setData(Uri.parse("package:" + context.getPackageName()));
            if (context instanceof Activity) {
                ((Activity) context).startActivityForResult(intent, 1001);
            } else {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        }
    }

    private static IntentSender createStatusReceiver(Context context, int sessionId) {
        Intent intent = new Intent(context, InstallResultReceiver.class);
        intent.setAction(InstallResultReceiver.ACTION_INSTALL_COMPLETE);
        intent.putExtra("session_id", sessionId);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                sessionId,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        return pendingIntent.getIntentSender();
    }

    // 安装结果广播接收器
    public static class InstallResultReceiver extends BroadcastReceiver {
        public static final String ACTION_INSTALL_COMPLETE = "INSTALL_COMPLETE_ACTION";

        @Override
        public void onReceive(Context context, Intent intent) {
            int status = intent.getIntExtra(PackageInstaller.EXTRA_STATUS, PackageInstaller.STATUS_FAILURE);
            int sessionId = intent.getIntExtra("session_id", -1);

            switch (status) {
                case PackageInstaller.STATUS_PENDING_USER_ACTION:
                    // 需要用户确认
                    Intent confirmationIntent = intent.getParcelableExtra(Intent.EXTRA_INTENT);
                    if (confirmationIntent != null) {
                        confirmationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(confirmationIntent);
                    }
                    break;

                case PackageInstaller.STATUS_SUCCESS:
                    Log.i("InstallResult", "安装成功");
                    break;

                default:
                    String errorMsg = intent.getStringExtra(PackageInstaller.EXTRA_STATUS_MESSAGE);
                    Log.e("InstallResult", "安装失败 (会话ID:" + sessionId + "): " + errorMsg);
            }
        }
    }
}

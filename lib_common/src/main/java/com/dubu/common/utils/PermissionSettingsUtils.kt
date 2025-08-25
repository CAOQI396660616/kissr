package com.dubu.common.utils
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log


/**
 *
 * 权限设置工具类 - 用于跳转到应用权限设置页面
 * @author cq
 * @date 2025/06/19
 */
object PermissionSettingsUtils {

    private const val TAG = "PermissionSettingsUtils"

    /**
     * 打开当前应用的权限设置页面
     *
     * @param context 上下文对象
     * @param requestCode 可选，用于 startActivityForResult
     */
    fun openAppPermissionSettings(context: Context, requestCode: Int? = null) {
        try {
            val intent = createPermissionSettingsIntent(context)

            // 检查 Intent 是否可用
            if (intent.resolveActivity(context.packageManager) != null) {
                if (requestCode != null && context is android.app.Activity) {
                    context.startActivityForResult(intent, requestCode)
                } else {
                    context.startActivity(intent)
                }
            } else {
                Log.w(TAG, "无法找到权限设置页面")
                fallbackToAppSettings(context, requestCode)
            }
        } catch (e: Exception) {
            Log.e(TAG, "打开权限设置失败", e)
            fallbackToAppSettings(context, requestCode)
        }
    }

    /**
     * 创建打开权限设置页面的 Intent
     */
    private fun createPermissionSettingsIntent(context: Context): Intent {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Android 8.0+ 使用专用权限设置 Intent
            Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = Uri.fromParts("package", context.packageName, null)
                putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
        } else {
            // 旧版本使用应用详情页面
            Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = Uri.fromParts("package", context.packageName, null)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
        }
    }

    /**
     * 备选方案：打开应用设置页面
     */
    private fun fallbackToAppSettings(context: Context, requestCode: Int? = null) {
        try {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = Uri.fromParts("package", context.packageName, null)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }

            if (intent.resolveActivity(context.packageManager) != null) {
                if (requestCode != null && context is android.app.Activity) {
                    context.startActivityForResult(intent, requestCode)
                } else {
                    context.startActivity(intent)
                }
            } else {
                Log.e(TAG, "无法打开应用设置页面")
            }
        } catch (e: Exception) {
            Log.e(TAG, "打开应用设置页面失败", e)
        }
    }

    /**
     * 创建打开特定权限设置页面的 Intent
     * 注意：并非所有设备都支持
     */
    @Suppress("unused")
//    fun createSpecificPermissionIntent(permission: String): Intent {
//        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
//                data = Uri.fromParts("package", context.packageName, null)
//                putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
//                putExtra("android.provider.extra.APP_PACKAGE", context.packageName)
//                putExtra("android.provider.extra.PERMISSION_NAME", permission)
//                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//            }
//        } else {
//            createPermissionSettingsIntent(context)
//        }
//    }

    /**
     * 检查是否应该显示权限说明（用户拒绝过但未选择"不再询问"）
     */
    fun shouldShowPermissionRationale(context: Context, permission: String): Boolean {
        return if (context is android.app.Activity) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                context.shouldShowRequestPermissionRationale(permission)
            } else {
                false
            }
        } else {
            false
        }
    }

    /**
     * 检查权限是否被永久拒绝（用户选择了"不再询问"）
     */
    fun isPermissionPermanentlyDenied(context: Context, permission: String): Boolean {
        return if (context is android.app.Activity) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // 用户拒绝过且不再询问
                !context.shouldShowRequestPermissionRationale(permission) &&
                        context.checkSelfPermission(permission) !=
                        android.content.pm.PackageManager.PERMISSION_GRANTED
            } else {
                false
            }
        } else {
            false
        }
    }
}
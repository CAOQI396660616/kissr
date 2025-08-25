package com.dubu.common.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

typealias PermissionCallback = (granted: Boolean) -> Unit

object PermissionsTool {
    const val RATIONAL_CODE = Activity.RESULT_FIRST_USER + 1000
    const val CODE_REQUEST_STORAGE = Activity.RESULT_FIRST_USER + 1001
    const val CODE_REQUEST_CAMERA = Activity.RESULT_FIRST_USER + 1002
    const val CODE_REQUEST_LOCATION = Activity.RESULT_FIRST_USER + 1003
    const val CODE_REQUEST_RECORD_AUDIO = Activity.RESULT_FIRST_USER + 1004
    const val CODE_REQUEST_CALL_PHONE = Activity.RESULT_FIRST_USER + 1005
    const val CODE_REQUEST_CALL_EXTRA_PHONE = Activity.RESULT_FIRST_USER + 1006


    fun grantedNotification(context: Context): Boolean {
        return NotificationManagerCompat.from(context).areNotificationsEnabled()
    }

    fun requestNotification(activity: Activity?) {
        activity?.startActivity(Intent().apply {
            action = "android.settings.APP_NOTIFICATION_SETTINGS"
            putExtra("android.provider.extra.APP_PACKAGE", activity.packageName)
        })
    }


    fun grantedLocation(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) != PackageManager.PERMISSION_DENIED
                || ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_DENIED
    }


    fun grantedStorage(context: Context): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_MEDIA_IMAGES
            ) != PackageManager.PERMISSION_DENIED
                    || ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_MEDIA_VIDEO
            ) != PackageManager.PERMISSION_DENIED
        } else {
            return ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_DENIED
                    || ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_DENIED
        }
    }

    fun grantedCallExtra(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CALL_PHONE
        ) != PackageManager.PERMISSION_DENIED
                || ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.READ_CALL_LOG
        ) != PackageManager.PERMISSION_DENIED
                || ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.WRITE_CALL_LOG
        ) != PackageManager.PERMISSION_DENIED
    }


    fun grantedCamera(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun grantedCall(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CALL_PHONE
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun requestStorage(context: Activity) {
        val a = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arrayOf(
                Manifest.permission.READ_MEDIA_IMAGES,
                Manifest.permission.READ_MEDIA_VIDEO,
            )
        } else {
            arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
            )
        }

        ActivityCompat.requestPermissions(
            context, a, CODE_REQUEST_STORAGE
        )
    }

    fun grantedRecordAudio(context: Context): Boolean {
        val checkSelfPermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.RECORD_AUDIO
        )
        return checkSelfPermission == PackageManager.PERMISSION_GRANTED
    }

    fun requestRecordAudio(context: Activity) {
        ActivityCompat.requestPermissions(
            context, arrayOf(
                Manifest.permission.RECORD_AUDIO,
            ), CODE_REQUEST_RECORD_AUDIO
        )
    }

    fun requestLocation(context: Activity) {
        ActivityCompat.requestPermissions(
            context, arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
            ), CODE_REQUEST_LOCATION
        )
    }

    fun requestCamera(context: Activity) {
        ActivityCompat.requestPermissions(
            context, arrayOf(
                Manifest.permission.CAMERA,
            ), CODE_REQUEST_CAMERA
        )
    }

    fun requestCall(context: Activity) {
        ActivityCompat.requestPermissions(
            context, arrayOf(
                Manifest.permission.CALL_PHONE,
            ), CODE_REQUEST_CALL_PHONE
        )
    }

    fun requestExtraCall(context: Activity) {
        ActivityCompat.requestPermissions(
            context, arrayOf(
                Manifest.permission.READ_CALL_LOG,
                Manifest.permission.WRITE_CALL_LOG,
            ), CODE_REQUEST_CALL_EXTRA_PHONE
        )
    }

    fun multiple(
        permissions: Array<String>,
        activity: AppCompatActivity,
        ret: (rational: ArrayList<String>, deniedPermissions: ArrayList<String>) -> Unit
    ): () -> Unit {
        val lc =
            activity.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { results ->
                val l = ArrayList<String>(4)
                val r = ArrayList<String>(4)
                for (d in results) {
                    if (!d.value) {
                        if (activity.shouldShowRequestPermissionRationale(d.key)) {
                            r.add(d.key)
                        } else {
                            l.add(d.key)
                        }
                    }
                }
                ret(r, l)
            }

        fun launch() {
            lc.launch(permissions)
        }

        return ::launch
    }


    @JvmStatic
    fun multiple(
        activity: AppCompatActivity,
        permissions: Array<String>,
        ret: (deniedPermissions: List<String>) -> Unit
    ): () -> Unit {
        val l =
            activity.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { results ->
                val d = ArrayList<String>(4)
                val r = ArrayList<String>(4)
                for (p in results) {
                    if (!p.value) {
                        if (activity.shouldShowRequestPermissionRationale(p.key)) {
                            r.add(p.key)
                        } else {
                            d.add(p.key)
                        }
                    }
                }
                ret(d)
                if (r.isNotEmpty()) {
                    ActivityCompat.requestPermissions(
                        activity,
                        r.toTypedArray(),
                        RATIONAL_CODE
                    )
                }
            }

        fun launch() {
            l.launch(permissions)
        }
        return ::launch
    }

    @JvmStatic
    fun single(
        activity: AppCompatActivity,
        permission: String,
        ret: PermissionCallback,
    ): () -> Unit {

        val l = activity.registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            ret(it)
        }

        fun launch() {
            l.launch(permission)
        }

        return ::launch
    }


    /**
     * must invoke after attach to activity
     * and before activity STARTED
     */
    @JvmStatic
    fun single(
        fragment: Fragment,
        permission: String,
        ret: PermissionCallback,
    ): (() -> Unit)? {
        val act = fragment.activity
        if (act == null || act.isFinishing || act.isDestroyed || act !is AppCompatActivity) return null
        return single(act, permission, ret)
    }


    fun isGranted(results: IntArray): Boolean {
        return results.isNotEmpty() && results[0] == PackageManager.PERMISSION_GRANTED
    }


    fun neededPermissions(activity: AppCompatActivity): Array<String> {
        val pl = ArrayList<String>()
        if (!grantedCamera(activity)) {
            pl.add(Manifest.permission.CAMERA)
        }


        if (!grantedStorage(activity)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                pl.add(Manifest.permission.READ_MEDIA_VIDEO)
                pl.add(Manifest.permission.READ_MEDIA_IMAGES)
            } else {
                pl.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                pl.add(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }

        if (!grantedRecordAudio(activity)) {
            pl.add(Manifest.permission.RECORD_AUDIO)
        }

   /*     if (!grantedCall(activity)) {
            pl.add(Manifest.permission.CALL_PHONE)
        }
        if (!grantedCallExtra(activity)) {
            pl.add(Manifest.permission.READ_CALL_LOG)
            pl.add(Manifest.permission.WRITE_CALL_LOG)
        }*/

        return pl.toTypedArray()
    }


    fun neededPermissionsV2(activity: AppCompatActivity): ArrayList<String> {
        val pl = ArrayList<String>()
        if (!grantedCamera(activity)) {
            pl.add(Manifest.permission.CAMERA)
        }


        if (!grantedStorage(activity)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                pl.add(Manifest.permission.READ_MEDIA_VIDEO)
                pl.add(Manifest.permission.READ_MEDIA_IMAGES)
            } else {
                pl.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                pl.add(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }

        if (!grantedRecordAudio(activity)) {
            pl.add(Manifest.permission.RECORD_AUDIO)
        }


        return pl
    }


    fun neededPermissionsV3(activity: AppCompatActivity): ArrayList<String> {
        val pl = ArrayList<String>()

        if (!grantedStorage(activity)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                pl.add(Manifest.permission.READ_MEDIA_VIDEO)
                pl.add(Manifest.permission.READ_MEDIA_IMAGES)
            } else {
                pl.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                pl.add(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }

        return pl
    }


}
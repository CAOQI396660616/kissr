package com.dubu.common.base

import android.app.Activity
import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.os.Process
import com.hjq.language.MultiLanguages
import java.lang.ref.WeakReference


abstract class BaseApp : Application(), Application.ActivityLifecycleCallbacks {

    companion object {
        lateinit var instance: BaseApp
        var topActivity: WeakReference<Activity>? = null
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(MultiLanguages.attach(base))
        instance = this
        registerActivityLifecycleCallbacks(this)
    }

    override fun onCreate() {
        backupWorkEngine()
        super.onCreate()
        mainWorkEngine()
    }

    protected abstract fun mainWorkEngine()
    protected abstract fun backupWorkEngine()


    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {

    }

    override fun onActivityStarted(activity: Activity) {

    }

    override fun onActivityResumed(activity: Activity) {
        topActivity = WeakReference(activity)
    }


    override fun onActivityPaused(activity: Activity) {
        topActivity?.clear()
    }

    override fun onActivityStopped(activity: Activity) {

    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

    }

    override fun onActivityDestroyed(activity: Activity) {
    }


    private fun exitAPP() {
        val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val list = activityManager.appTasks
        for (appTask in list) {
            appTask.finishAndRemoveTask()
        }
    }

    private fun getCurrentProcessName(context: Context): String? {
        val pid = Process.myPid()
        val am = context.getSystemService(ACTIVITY_SERVICE) as ActivityManager
        for (appProcess in am.runningAppProcesses) {
            if (appProcess.pid == pid) {
                return appProcess.processName
            }
        }
        return null
    }

}



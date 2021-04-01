package com.theo.llog

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.theo.llog.permission.PermissionUtils

internal object LifecycleManager {
    private var isInstall = false
    private var activityCount = 0

    fun setLifecycleCallbacks(app: Application) {
        app.registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) = Unit

            override fun onActivityStarted(activity: Activity) {
                activityCount++
                checkStart(activity)
            }

            override fun onActivityResumed(activity: Activity) {
                checkShow()
            }

            override fun onActivityPaused(activity: Activity) = Unit

            override fun onActivityStopped(activity: Activity) {
                activityCount--
                checkHide(activity)
            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) = Unit

            override fun onActivityDestroyed(activity: Activity) = Unit
        })
    }

    fun isForeground(): Boolean {
        return activityCount > 0
    }

    private fun checkStart(activity: Activity) {
        if (activityCount > 0 && !isInstall) {
            if (PermissionUtils.checkPermission(activity)) {
                LLog.start(activity)
            } else {
                PermissionUtils.requestPermission(activity) { granted ->
                    if (granted) {
                        LLog.start(activity)
                    }
                }
            }
            isInstall = true
        }
    }

    private fun checkShow() {
        LLog.mainIcon?.run { setVisible(needShow) }
        LLog.monitor?.run { setVisible(needShow) }
    }

    private fun checkHide(activity: Activity) {
        if (!activity.isFinishing && isForeground()) {
            return
        }
        LLog.mainIcon?.setVisible(false)
        LLog.monitor?.setVisible(false)
    }
}

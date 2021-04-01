package com.theo.llog

import android.app.Activity
import android.app.Application
import com.theo.llog.floating.FloatingMainIcon
import com.theo.llog.floating.FloatingMonitor
import com.theo.llog.logcat.LogcatManager

object LLog {
    internal var mainIcon: FloatingMainIcon? = null
    internal var monitor: FloatingMonitor? = null

    @JvmStatic
    fun install(application: Application) {
        LifecycleManager.setLifecycleCallbacks(application)
    }

    @JvmStatic
    internal fun start(activity: Activity) {
        monitor = FloatingMonitor(activity).apply { create() }
        mainIcon = FloatingMainIcon(activity).apply {
            create()
            show()
        }
        LogcatManager.start()
    }
}
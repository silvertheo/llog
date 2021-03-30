package com.theo.sample.llog

import android.app.Application
import com.theo.llog.LLog

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        LLog.install(this)
//        DoraemonKit.install(this)
    }
}
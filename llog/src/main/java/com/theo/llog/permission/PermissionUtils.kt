package com.theo.llog.permission

import android.app.Activity
import android.app.Fragment
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings

object PermissionUtils {
    internal const val REQUEST_PERMISSION = 5272

    fun interface RequestCallback {
        fun onResult(granted: Boolean)
    }

    @JvmStatic
    fun checkPermission(context: Context): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return Settings.canDrawOverlays(context)
        }
        return true
    }

    @JvmStatic
    fun requestPermission(activity: Activity, callback: RequestCallback) {
        PermissionFragment.requestPermission(activity, callback)
    }

    @Suppress("DEPRECATION")
    internal fun requestPermission(fragment: Fragment) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activity = fragment.activity ?: return
            val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
            intent.data = Uri.parse("package:" + activity.packageName)
            fragment.startActivityForResult(intent, REQUEST_PERMISSION)
        }
    }
}


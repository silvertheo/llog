package com.theo.llog.permission

import android.app.Activity
import android.app.Fragment
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper

@Suppress("DEPRECATION")
internal class PermissionFragment : Fragment() {

    companion object {
        private var requestCallback: PermissionUtils.RequestCallback? = null

        fun requestPermission(activity: Activity, callback: PermissionUtils.RequestCallback) {
            requestCallback = callback
            activity.fragmentManager
                .beginTransaction()
                .add(PermissionFragment(), activity.localClassName)
                .commitAllowingStateLoss()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        PermissionUtils.requestPermission(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == PermissionUtils.REQUEST_PERMISSION) {
            Handler(Looper.getMainLooper()).postDelayed({
                val activity = activity ?: return@postDelayed
                val granted = PermissionUtils.checkPermission(activity)
                requestCallback?.onResult(granted)
                requestCallback = null
                fragmentManager.beginTransaction().remove(this).commitAllowingStateLoss()
            }, 500)
        }
    }
}

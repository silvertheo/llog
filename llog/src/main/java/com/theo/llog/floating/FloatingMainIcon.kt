package com.theo.llog.floating

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.theo.llog.LLog
import com.theo.llog.R

internal class FloatingMainIcon(context: Context) : FloatingView(context) {

    override fun onCreateView(context: Context, root: FrameLayout): View {
        return LayoutInflater.from(context).inflate(R.layout.llog_main_icon, root, true)
    }

    override fun onViewCreated(view: View) {
        view.setOnClickListener {
            hide()
            LLog.monitor?.show()
        }
    }
}
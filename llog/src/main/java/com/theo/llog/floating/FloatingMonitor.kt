package com.theo.llog.floating

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.RadioGroup
import android.widget.RelativeLayout
import androidx.recyclerview.widget.RecyclerView
import com.theo.llog.LLog
import com.theo.llog.R
import com.theo.llog.ext.dp
import com.theo.llog.logcat.LogLineAdapter
import com.theo.llog.logcat.LogcatManager
import kotlin.math.max

internal class FloatingMonitor(context: Context) : FloatingView(context) {

    override fun onCreateView(context: Context, root: FrameLayout): View {
        return LayoutInflater.from(context).inflate(R.layout.llog_monitor, root, true)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View) {
        val layout = view.findViewById<RelativeLayout>(R.id.llog_monitor_layout)
        val list = view.findViewById<RecyclerView>(R.id.llog_monitor_list)
        val dragger = view.findViewById<ScaleDragger>(R.id.llog_monitor_scale)
        val btnLevels = view.findViewById<RadioGroup>(R.id.llog_monitor_levels)
        val btnClose = view.findViewById<ImageView>(R.id.llog_monitor_close)
        val adapter = LogLineAdapter(context)
        list?.adapter = adapter
        list?.addOnItemTouchListener(object : RecyclerView.OnItemTouchListener {
            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                list.parent.requestDisallowInterceptTouchEvent(true)
                return false
            }

            override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) = Unit

            override fun onRequestDisallowInterceptTouchEvent(disallow: Boolean) = Unit
        })
        LogcatManager.registerListener { logLines ->
            if (logLines.size == 1) {
                adapter.addLog(logLines[0], true)
            } else {
                logLines.forEach { adapter.addLog(it, false) }
                adapter.notifyDataSetChanged()
            }
            list?.scrollToPosition(adapter.itemCount - 1)
        }
        dragger.setOnScaledListener { _, x, y ->
            val params = layout.layoutParams as FrameLayout.LayoutParams
            params.width = max(params.width + x, 204.dp)
            params.height = max(params.height + y, 220.dp)
            layout.layoutParams = params
        }
        btnLevels.setOnCheckedChangeListener { _, checkedId ->
            adapter.setLimitLevel(
                when (checkedId) {
                    R.id.llog_monitor_level_v -> Log.VERBOSE
                    R.id.llog_monitor_level_d -> Log.DEBUG
                    R.id.llog_monitor_level_i -> Log.INFO
                    R.id.llog_monitor_level_w -> Log.WARN
                    R.id.llog_monitor_level_e -> Log.ERROR
                    R.id.llog_monitor_level_a -> Log.ASSERT
                    else -> Log.VERBOSE
                }
            )
            list?.scrollToPosition(adapter.itemCount - 1)
        }
        btnClose.setOnClickListener {
            hide()
            LLog.mainIcon?.show()
        }
    }
}
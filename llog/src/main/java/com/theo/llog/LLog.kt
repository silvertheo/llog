package com.theo.llog

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.res.Resources
import android.util.Log
import android.util.TypedValue
import android.view.MotionEvent
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.RadioGroup
import android.widget.RelativeLayout
import androidx.recyclerview.widget.RecyclerView
import com.lzf.easyfloat.EasyFloat
import com.lzf.easyfloat.enums.ShowPattern
import com.theo.llog.logcat.LogLineAdapter
import com.theo.llog.logcat.LogcatManager
import com.theo.llog.widget.ScaleDragger
import java.lang.Integer.max

object LLog {

    @JvmStatic
    fun install(application: Application) {
        createIcon(application)
        LogcatManager.start()
    }

    private fun createIcon(context: Context) {
        EasyFloat.with(context)
            .setTag("LLOG_ICON")
            .setShowPattern(ShowPattern.FOREGROUND)
            .setAnimator(null)
            .setLayout(R.layout.llog_icon) {
                it.setOnClickListener {
                    if (EasyFloat.getFloatView("LLOG_MONITOR") == null) {
                        createMonitor(context)
                    } else {
                        EasyFloat.show("LLOG_MONITOR")
                    }
                    EasyFloat.hide("LLOG_ICON")
                }
            }.show()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun createMonitor(context: Context) {
        EasyFloat.with(context)
            .setTag("LLOG_MONITOR")
            .setShowPattern(ShowPattern.FOREGROUND)
            .setAnimator(null)
            .hasEditText(true)
            .setLayout(R.layout.llog_monitor) {
                val layout = it.findViewById<RelativeLayout>(R.id.llog_monitor_layout)
                val params = layout.layoutParams as FrameLayout.LayoutParams
                val scaleHelper = layout.findViewById<ScaleDragger>(R.id.llog_monitor_scale)
                scaleHelper.onScaledListener = object : ScaleDragger.OnScaledListener {
                    override fun onScaled(x: Float, y: Float, event: MotionEvent) {
                        params.width = max(params.width + x.toInt(), dp(204))
                        params.height = max(params.height + y.toInt(), dp(220))
                        layout.layoutParams = params
                    }
                }
                val list = layout.findViewById<RecyclerView>(R.id.llog_monitor_list)
                val adapter = LogLineAdapter(context)
                list?.adapter = adapter
                list?.setOnTouchListener { _, event ->
                    EasyFloat.dragEnable(event.action == MotionEvent.ACTION_UP, "LLOG_MONITOR")
                    false
                }
                list?.addOnItemTouchListener(object : RecyclerView.OnItemTouchListener {
                    override fun onInterceptTouchEvent(
                        rv: RecyclerView,
                        e: MotionEvent
                    ): Boolean {
                        EasyFloat.dragEnable(e.action == MotionEvent.ACTION_UP, "LLOG_MONITOR")
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
                layout.findViewById<ImageView>(R.id.llog_monitor_close)
                    .setOnClickListener {
                        EasyFloat.show("LLOG_ICON")
                        EasyFloat.hide("LLOG_MONITOR")
                    }
                layout.findViewById<RadioGroup>(R.id.llog_monitor_levels)
                    .setOnCheckedChangeListener { group, checkedId ->
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
            }.show()
    }

    private fun dp(value: Number): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            value.toFloat(),
            Resources.getSystem().displayMetrics
        ).toInt()
    }
}
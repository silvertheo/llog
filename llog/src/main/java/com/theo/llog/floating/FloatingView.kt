package com.theo.llog.floating

import android.app.Service
import android.content.Context
import android.graphics.PixelFormat
import android.os.Build
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.view.WindowManager.LayoutParams.*
import android.widget.FrameLayout
import com.theo.llog.LifecycleManager
import com.theo.llog.ext.dp


internal abstract class FloatingView(val context: Context) {
    private var windowManager = context.getSystemService(Service.WINDOW_SERVICE) as WindowManager
    private var windowLayoutParams = WindowManager.LayoutParams()
    private var touchHelper = TouchHelper(context, true)
    private var frameLayout = FloatingFrameLayout(context)
    var needShow = false

    abstract fun onCreateView(context: Context, root: FrameLayout): View

    abstract fun onViewCreated(view: View)

    fun create() {
        initParams()
        addView()
    }

    fun show() {
        needShow = true
        setVisible(needShow)
    }

    fun hide() {
        needShow = false
        setVisible(needShow)
    }

    fun setVisible(visible: Boolean) {
        frameLayout.visibility = if (visible) View.VISIBLE else View.GONE
    }

    private fun initParams() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            windowLayoutParams.type = TYPE_APPLICATION_OVERLAY
        } else {
            windowLayoutParams.type = TYPE_PHONE
        }
        windowLayoutParams.format = PixelFormat.RGBA_8888
        windowLayoutParams.gravity = Gravity.START or Gravity.TOP
        windowLayoutParams.flags = FLAG_NOT_TOUCH_MODAL or FLAG_NOT_FOCUSABLE
        windowLayoutParams.width = WRAP_CONTENT
        windowLayoutParams.height = WRAP_CONTENT
        windowLayoutParams.y = 56.dp
    }

    private fun addView() {
        frameLayout = FloatingFrameLayout(context).apply {
            onCreateView(context, this)
            visibility = View.INVISIBLE
            windowManager.addView(this, windowLayoutParams)
            setOnTouchListener { event ->
                touchHelper.updateFloat(this, event, windowManager, windowLayoutParams)
            }
            setOnLayoutListener {
                if (!LifecycleManager.isForeground()) {
                    visibility = View.GONE
                } else {
                    visibility = if (needShow) View.VISIBLE else View.GONE
                }
            }
            onViewCreated(this)
        }
    }
}
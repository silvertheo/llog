package com.theo.llog.floating

import android.content.Context
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import com.theo.llog.ext.dp
import com.theo.llog.ext.screenHeight
import com.theo.llog.ext.screenWidth

internal class TouchHelper(val context: Context, var canDrag: Boolean) {
    private var isDrag = false

    private var screenWidth = 0
    private var screenHeight = 0

    private var borderLeft = 0
    private var borderTop = 0
    private var borderRight = 0
    private var borderBottom = 0

    private var lastX = 0f
    private var lastY = 0f

    fun updateFloat(
        view: View,
        event: MotionEvent,
        windowManager: WindowManager,
        windowLayoutParams: WindowManager.LayoutParams
    ) {
        if (!canDrag) {
            return
        }
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                isDrag = false
                lastX = event.rawX
                lastY = event.rawY

                screenWidth = context.screenWidth
                screenHeight = context.screenHeight
                borderLeft = 0
                borderRight = screenWidth - view.width
                borderTop = 0
                borderBottom = screenHeight - view.height
            }
            MotionEvent.ACTION_MOVE -> {
                if (event.rawX < borderLeft
                    || event.rawX > borderRight + view.width
                    || event.rawY < borderTop
                    || event.rawY > borderBottom + view.height
                ) {
                    return
                }
                val dx = event.rawX - lastX
                val dy = event.rawY - lastY
                if (!isDrag && dx * dx + dy * dy < 20.dp) {
                    return
                }
                isDrag = true
                var x = windowLayoutParams.x + dx.toInt()
                var y = windowLayoutParams.y + dy.toInt()
                x = when {
                    x < borderLeft -> borderLeft
                    x > borderRight -> borderRight
                    else -> x
                }
                y = when {
                    y < borderTop -> borderTop
                    y > borderBottom -> borderBottom
                    else -> y
                }
                windowLayoutParams.x = x
                windowLayoutParams.y = y
                windowManager.updateViewLayout(view, windowLayoutParams)
                lastX = event.rawX
                lastY = event.rawY
            }
        }
    }
}
package com.theo.llog.floating

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatImageView

internal class ScaleDragger(context: Context, attrs: AttributeSet? = null) :
    AppCompatImageView(context, attrs) {

    private var scaledListener: OnScaledListener? = null
    private var lastX = 0f
    private var lastY = 0f

    fun interface OnScaledListener {
        fun onScaled(event: MotionEvent, x: Int, y: Int)
    }

    fun setOnScaledListener(listener: OnScaledListener) {
        scaledListener = listener
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        parent.requestDisallowInterceptTouchEvent(true)
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                lastX = event.x
                lastY = event.y
            }
            MotionEvent.ACTION_UP -> {
                scaledListener?.onScaled(
                    event,
                    (event.x - lastX).toInt(),
                    (event.y - lastY).toInt()
                )
            }
        }
        return true
    }
}
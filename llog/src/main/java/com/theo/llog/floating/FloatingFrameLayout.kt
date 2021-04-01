package com.theo.llog.floating

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.FrameLayout

internal class FloatingFrameLayout(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var touchListener: OnTouchListener? = null
    private var layoutListener: OnLayoutListener? = null
    private var isCreated = false

    fun interface OnTouchListener {
        fun onTouch(event: MotionEvent)
    }

    fun interface OnLayoutListener {
        fun onLayout()
    }

    fun setOnTouchListener(listener: OnTouchListener) {
        touchListener = listener
    }

    fun setOnLayoutListener(listener: OnLayoutListener) {
        layoutListener = listener
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        if (!isCreated) {
            isCreated = true
            layoutListener?.onLayout()
        }
    }

    override fun onInterceptTouchEvent(event: MotionEvent?): Boolean {
        event?.let { touchListener?.onTouch(it) }
        return super.onInterceptTouchEvent(event)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.let { touchListener?.onTouch(it) }
        return super.onTouchEvent(event)
    }
}
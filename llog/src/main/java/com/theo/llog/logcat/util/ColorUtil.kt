package com.theo.llog.logcat.util

import android.content.Context
import android.util.Log
import android.util.SparseIntArray
import androidx.core.content.ContextCompat
import com.theo.llog.R

object ColorUtil {
    private val TEXT_COLOR = SparseIntArray(6)
    private val LEVEL_COLOR = SparseIntArray(6)

    init {
        TEXT_COLOR.put(Log.VERBOSE, R.color.llog_text_v)
        TEXT_COLOR.put(Log.DEBUG, R.color.llog_text_d)
        TEXT_COLOR.put(Log.INFO, R.color.llog_text_i)
        TEXT_COLOR.put(Log.WARN, R.color.llog_text_w)
        TEXT_COLOR.put(Log.ERROR, R.color.llog_text_e)
        TEXT_COLOR.put(Log.ASSERT, R.color.llog_text_a)

        LEVEL_COLOR.put(Log.VERBOSE, R.color.llog_level_v)
        LEVEL_COLOR.put(Log.DEBUG, R.color.llog_level_d)
        LEVEL_COLOR.put(Log.INFO, R.color.llog_level_i)
        LEVEL_COLOR.put(Log.WARN, R.color.llog_level_w)
        LEVEL_COLOR.put(Log.ERROR, R.color.llog_level_e)
        LEVEL_COLOR.put(Log.ASSERT, R.color.llog_level_a)
    }

    @JvmStatic
    fun getTextColor(context: Context, level: Int): Int {
        val result = TEXT_COLOR.get(level, TEXT_COLOR[Log.VERBOSE])
        return ContextCompat.getColor(context, result)
    }

    @JvmStatic
    fun getLevelColor(context: Context, level: Int): Int {
        val result = LEVEL_COLOR.get(level, LEVEL_COLOR[Log.VERBOSE])
        return ContextCompat.getColor(context, result)
    }
}
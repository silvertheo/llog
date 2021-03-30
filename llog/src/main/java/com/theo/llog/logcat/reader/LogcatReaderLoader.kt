package com.theo.llog.logcat.reader

import com.theo.llog.logcat.helper.LogcatHelper
import java.io.IOException

class LogcatReaderLoader private constructor(
    buffers: List<String>,
    private var recordingMode: Boolean
) {
    companion object {
        @JvmStatic
        fun create(recordingMode: Boolean): LogcatReaderLoader {
            val buffers: ArrayList<String> = arrayListOf()
            buffers.add("main")
            return LogcatReaderLoader(buffers, recordingMode)
        }
    }

    private val lastLines: HashMap<String, String?> = hashMapOf()
    private var multiple: Boolean = false

    init {
        this.multiple = buffers.size > 1
        for (buffer in buffers) {
            val lastLine = if (recordingMode) LogcatHelper.getLastLogLine(buffer) else null
            lastLines[buffer] = lastLine
        }
    }

    @Throws(IOException::class)
    fun loadReader(): LogcatReader {
        val buffer = lastLines.keys.iterator().next()
        val lastLine = lastLines.values.iterator().next()
        return SingleLogcatReader(recordingMode, buffer, lastLine)
    }
}
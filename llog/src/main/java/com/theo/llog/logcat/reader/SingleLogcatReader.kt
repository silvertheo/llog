package com.theo.llog.logcat.reader

import com.theo.llog.logcat.helper.LogcatHelper
import com.theo.llog.logcat.helper.RuntimeHelper
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

class SingleLogcatReader @Throws(IOException::class) constructor(
    recordingMode: Boolean,
    private val logBuffer: String,
    private var lastLine: String?
) : AbsLogcatReader(recordingMode) {
    private var logcatProcess: Process? = null
    private lateinit var bufferedReader: BufferedReader

    init {
        init()
    }

    @Throws(IOException::class)
    private fun init() {
        logcatProcess = LogcatHelper.getLogcatProcess(logBuffer)
        bufferedReader = BufferedReader(InputStreamReader(logcatProcess!!.inputStream), 8 * 1024)
    }

    @Throws(IOException::class)
    override fun readLine(): String? {
        val line = bufferedReader.readLine()
        if (recordingMode && lastLine != null) {
            if (lastLine == line) {
                lastLine = null
            }
        }
        return line
    }

    override fun closeQuietly() {
        RuntimeHelper.destroy(logcatProcess)
    }

    override fun readyToRecord(): Boolean {
        return recordingMode && lastLine == null
    }

    override fun getProcesses(): List<Process?> {
        return listOf(logcatProcess)
    }
}
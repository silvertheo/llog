package com.theo.llog.logcat

import android.os.Handler
import android.os.Looper
import android.os.Message
import com.theo.llog.logcat.reader.LogcatReaderLoader
import com.theo.llog.logcat.util.ExecutorUtil
import java.io.IOException
import java.util.*

object LogcatManager {
    const val MSG_PUBLISH_LOG = 109

    private var logCatchListener: LogCatchListener? = null
    private var logCatchRunnable: LogCatchRunnable? = null

    fun start() {
        logCatchRunnable?.stop()
        logCatchRunnable = LogCatchRunnable().apply {
            ExecutorUtil.execute(this)
        }
    }

    fun stop() {
        logCatchRunnable?.stop()
    }


    fun interface LogCatchListener {
        fun onLogCatch(logLines: List<LogLine>)
    }

    fun registerListener(listener: LogCatchListener?) {
        logCatchListener = listener
    }

    fun unregisterListener() {
        logCatchListener = null
    }

    private class InternalHandler(looper: Looper) : Handler(looper) {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                MSG_PUBLISH_LOG -> logCatchListener?.onLogCatch(msg.obj as List<LogLine>)
            }
        }
    }

    private class LogCatchRunnable : Runnable {
        private var isRunning: Boolean = true
        private var handler: InternalHandler? = null
        private var pid: Int = 0

        init {
            handler = InternalHandler(Looper.getMainLooper())
            pid = android.os.Process.myPid()
        }

        override fun run() {
            try {
                val logcatReader = LogcatReaderLoader.create(true).loadReader()
                val initialLines = LinkedList<LogLine>()
                var line: String?
                while (logcatReader.readLine().also { line = it } != null && isRunning) {
                    val logLine = LogLine.create(line!!, false)
                    if (!logcatReader.readyToRecord()) {
                        if (logLine.processId == pid) {
                            initialLines.add(logLine)
                        }
                        if (initialLines.size > 10000) {
                            initialLines.removeFirst()
                        }
                    } else if (initialLines.isNotEmpty()) {
                        if (logLine.processId == pid) {
                            initialLines.add(logLine)
                        }
                        Message.obtain().apply {
                            what = MSG_PUBLISH_LOG
                            obj = ArrayList(initialLines)
                            handler?.sendMessage(this)
                        }
                        initialLines.clear()
                    } else {
                        if (logLine.processId == pid) {
                            Message.obtain().apply {
                                what = MSG_PUBLISH_LOG
                                obj = arrayListOf(logLine)
                                handler?.sendMessage(this)
                            }
                        }
                    }
                }
                logcatReader.closeQuietly()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        fun stop() {
            isRunning = false
        }
    }
}
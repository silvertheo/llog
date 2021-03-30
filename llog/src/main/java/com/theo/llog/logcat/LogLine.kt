package com.theo.llog.logcat

import android.util.Log
import java.util.regex.Pattern

class LogLine {
    var timestamp: String? = null
    var processId: Int = -1
    var logLevel: Int = 0
    var tag: String? = null
    var isExpanded = false
    var isHighlighted = false

    var logOutput: String? = null
        private set

    val logLevelText: String
        get() = convertLogLevelToChar(logLevel).toString()

    val originalLine: String?
        get() {
            if (logLevel == -1) {
                return logOutput
            }
            return StringBuilder()
                .append(if (timestamp != null) "$timestamp " else "")
                .append("$logLevelText/$tag($processId): $logOutput")
                .toString()
        }

    companion object {
        private const val TIMESTAMP_LENGTH = 19
        private val logPattern = Pattern.compile("(\\w)/([^(]+)\\(\\s*(\\d+)(?:\\*\\s*\\d+)?\\): ")

        @JvmStatic
        fun create(log: String, expanded: Boolean): LogLine {
            val logLine = LogLine()
            logLine.isExpanded = expanded
            var startIndex = 0
            if (log.isNotBlank() && log[0].isDigit() && log.length >= TIMESTAMP_LENGTH) {
                logLine.timestamp = log.substring(0, TIMESTAMP_LENGTH - 1)
                startIndex = TIMESTAMP_LENGTH
            }
            val matcher = logPattern.matcher(log)
            if (matcher.find(startIndex)) {
                logLine.logLevel = convertCharToLogLevel(matcher.group(1)[0])
                logLine.tag = matcher.group(2).trim()
                logLine.processId = matcher.group(3).toInt()
                logLine.logOutput = log.substring(matcher.end())
            } else {
                logLine.logOutput = log
                logLine.logLevel = -1
            }
            return logLine
        }

        private fun convertCharToLogLevel(logLevelChar: Char): Int {
            return when (logLevelChar) {
                'V' -> Log.VERBOSE
                'D' -> Log.DEBUG
                'I' -> Log.INFO
                'W' -> Log.WARN
                'E' -> Log.ERROR
                else -> -1
            }
        }

        private fun convertLogLevelToChar(logLevel: Int): Char {
            return when (logLevel) {
                Log.VERBOSE -> 'V'
                Log.DEBUG -> 'D'
                Log.INFO -> 'I'
                Log.WARN -> 'W'
                Log.ERROR -> 'E'
                else -> ' '
            }
        }
    }
}
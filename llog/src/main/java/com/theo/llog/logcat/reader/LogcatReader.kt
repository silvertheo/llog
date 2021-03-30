package com.theo.llog.logcat.reader

import java.io.IOException

interface LogcatReader {

    @Throws(IOException::class)
    fun readLine(): String?

    fun closeQuietly()

    fun readyToRecord(): Boolean

    fun getProcesses(): List<Process?>
}
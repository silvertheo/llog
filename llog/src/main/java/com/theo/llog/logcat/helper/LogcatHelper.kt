package com.theo.llog.logcat.helper

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

object LogcatHelper {
    private const val BUFFER_MAIN = "main"

    @JvmStatic
    @Throws(IOException::class)
    fun getLogcatProcess(buffer: String): Process {
        return RuntimeHelper.exec(getLogcatArgs(buffer))
    }

    private fun getLogcatArgs(buffer: String): List<String> {
        val args = arrayListOf("logcat", "-v", "time")
        if (buffer == BUFFER_MAIN) {
            args.add("-b")
            args.add(buffer)
        }
        return args
    }

    @JvmStatic
    fun getLastLogLine(buffer: String): String? {
        var dumpLogcatProcess: Process? = null
        var result: String? = null
        try {
            val args = getLogcatArgs(buffer).toMutableList()
            args.add("-d")
            dumpLogcatProcess = RuntimeHelper.exec(args)
            val size = 8 * 1024
            val reader = BufferedReader(InputStreamReader(dumpLogcatProcess.inputStream), size)
            while(true) { result = reader.readLine() ?: break }
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            RuntimeHelper.destroy(dumpLogcatProcess)
        }
        return result
    }
}
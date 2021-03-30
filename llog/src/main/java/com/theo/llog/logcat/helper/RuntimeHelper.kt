package com.theo.llog.logcat.helper

import java.io.IOException

object RuntimeHelper {

    @JvmStatic
    @Throws(IOException::class)
    fun exec(args: List<String>): Process {
        return Runtime.getRuntime().exec(args.toTypedArray())
    }

    @JvmStatic
    fun destroy(process: Process?) {
        process?.destroy()
    }
}
package com.theo.llog.logcat.util

import java.util.concurrent.ExecutorService
import java.util.concurrent.SynchronousQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

object ExecutorUtil {
    private var executorService: ExecutorService? = null

    @JvmStatic
    fun execute(runnable: Runnable) {
        if (executorService == null) {
            executorService = ThreadPoolExecutor(
                1, 5, 60L, TimeUnit.SECONDS,
                SynchronousQueue(),
                ThreadPoolExecutor.AbortPolicy()
            )
        }
        executorService?.execute(runnable)
    }
}
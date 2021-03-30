package com.theo.llog.logcat.reader

abstract class AbsLogcatReader(
    protected var recordingMode: Boolean
) : LogcatReader {

    fun isRecordingMode(): Boolean {
        return recordingMode
    }
}
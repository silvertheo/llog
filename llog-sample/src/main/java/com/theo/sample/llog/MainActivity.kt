package com.theo.sample.llog

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    companion object {
        const val TAG = "Main"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.button1).setOnClickListener {
            Log.v(TAG, "这是一条普通的日志。超长内容哦，超长内容哦，超长内容哦，超长内容哦，超长内容哦。")
        }
        findViewById<Button>(R.id.button2).setOnClickListener {
            Log.v(TAG, "这是一条日志 V")
            Log.d(TAG, "这是一条日志 D")
            Log.i(TAG, "这是一条日志 I")
            Log.w(TAG, "这是一条日志 W")
            Log.e(TAG, "这是一条日志 E")
            Log.wtf(TAG, "这是一条日志 WTF")
        }
        findViewById<Button>(R.id.button3).setOnClickListener {
            Log.e(TAG, "这是一条异常日志", RuntimeException())
        }
    }
}
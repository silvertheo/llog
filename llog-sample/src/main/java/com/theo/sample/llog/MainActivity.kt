package com.theo.sample.llog

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.button1).setOnClickListener {
            Log.v(
                "Main",
                "这是一条普通的日志 超长内容哦 超长内容哦 超长内容哦 超长内容哦 超长内容哦 超长内容哦 超长内容哦 超长内容哦 超长内容哦 超长内容哦 超长内容哦 超长内容哦 超长内容哦 超长内容哦 超长内容哦 超长内容哦 超长内容哦 超长内容哦 超长内容哦 超长内容哦 超长内容哦 超长内容哦"
            )
        }
        findViewById<Button>(R.id.button2).setOnClickListener {
            Log.v("Main", "你好 V")
            Log.d("Main", "你好 D")
            Log.i("Main", "你好 I")
            Log.w("Main", "你好 W")
            Log.e("Main", "你好 E")
            Log.wtf("Main", "你好 WTF")
        }
        findViewById<Button>(R.id.button3).setOnClickListener {
            Log.e("Main", "RuntimeException", RuntimeException())
        }
//
//        val web = findViewById<WebView>(R.id.web)
//        web.settings.run {
//            javaScriptEnabled = true
//        }
//        web.loadUrl("http://192.168.160.16:3000/#/pharmacist/login")
    }
}
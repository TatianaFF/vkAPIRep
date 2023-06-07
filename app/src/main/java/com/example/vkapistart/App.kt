package com.example.vkapistart

import android.app.Application
import android.widget.Toast
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKTokenExpiredHandler

class App : Application() {
    override fun onCreate() {
        super.onCreate()

//        Utils.initSuperAppKit(this)
        VK.addTokenExpiredHandler(tokenTracker)
    }

    private val tokenTracker = object: VKTokenExpiredHandler {
        override fun onTokenExpired() {
            // token expired
            Toast.makeText(this@App, "token expired", Toast.LENGTH_SHORT).show()
        }
    }
}
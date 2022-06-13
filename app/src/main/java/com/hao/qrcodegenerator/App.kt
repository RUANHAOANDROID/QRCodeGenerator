package com.hao.qrcodegenerator

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        context = this
    }

    companion object {
        /**
         * 提供单一全局context
         */
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context

    }
}
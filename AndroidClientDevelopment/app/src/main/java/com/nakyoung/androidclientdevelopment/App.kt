package com.nakyoung.androidclientdevelopment

import android.app.Application
import com.nakyoung.androidclientdevelopment.api.ApiService

class App: Application() {

    override fun onCreate() {
        super.onCreate()

        ApiService.init(this)
    }
}
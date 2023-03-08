package com.nakyoung.androidclientdevelopment

import android.app.Application
import com.nakyoung.androidclientdevelopment.manager.AuthManager
import com.nakyoung.androidclientdevelopment.service.ApiService

/**
 * 싱글톤으로 생성되어야할 것들은
 * 모두 App에 떄려박는다.
 * **/
class App: Application() {

    override fun onCreate() {
        super.onCreate()

        AuthManager.init(this)
        ApiService.init(this)
    }
}
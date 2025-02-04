package com.nakyoung.androidclientdevelopment.manager

import android.content.Context
import android.content.SharedPreferences

object AuthManager {
    const val UID = "uid"
    const val ACCESS_TOKEN = "access_token"
    const val REFRESH_TOKEN = "refresh_token"

    lateinit var prefs: SharedPreferences

    fun init(context: Context){
        prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
    }

    /**Commit your preferences changes back from this Editor to the SharedPreferences object it is editing.
     * This atomically performs the requested modifications, replacing whatever is currently in the SharedPreferences.**/
    var uid: String?
        get() = prefs.getString(UID, null)
        set(value) {
            prefs.edit().putString(UID,value).apply()
        }

    var accessToken: String?
        get() = prefs.getString(ACCESS_TOKEN, null)
        set(value) {
            prefs.edit().putString(ACCESS_TOKEN, value).apply()
        }

    var refreshToken: String?
        get() = prefs.getString(REFRESH_TOKEN, null)
        set(value) {
            prefs.edit().putString(REFRESH_TOKEN, value).apply()
        }

    fun clear(){
        prefs.edit().clear().apply()
    }
}

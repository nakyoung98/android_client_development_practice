package com.nakyoung.androidclientdevelopment.api

import com.nakyoung.androidclientdevelopment.manager.AuthManager
import okhttp3.Interceptor
import okhttp3.Response

/**
 * 애플리케이션 인터셉터
 * **/
class AuthInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val builder = request.newBuilder()

        AuthManager.accessToken?.let{ token ->
            builder.header("Authorization", "Bearer $token")
        }
        return chain.proceed(builder.build())
    }
}
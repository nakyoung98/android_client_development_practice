package com.nakyoung.androidclientdevelopment.api

import com.nakyoung.androidclientdevelopment.manager.AuthManager
import com.nakyoung.androidclientdevelopment.statics.AuthType
import okhttp3.Interceptor
import okhttp3.Response

/**
 * 애플리케이션 인터셉터
 * **/
class AuthInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val builder = request.newBuilder()


        // 질문을 가져올 때 Authorization이 있는지
        // 토큰을 갱신할 때 Authrization 헤더가 없는지
        // 로그인된 상태에서 다시 로그인을 했을 때 Authorization 헤더가 없는지 확인
        val authType = request.tag(AuthType::class.java) ?: AuthType.ACCESS_TOKEN
        when (authType) {
            AuthType.NO_AUTH -> {}
            AuthType.ACCESS_TOKEN -> {
                AuthManager.accessToken?.let { token ->
                    builder.header("Authorization", "Bearer $token")
                }
            }
        }

        return chain.proceed(builder.build())
    }
}
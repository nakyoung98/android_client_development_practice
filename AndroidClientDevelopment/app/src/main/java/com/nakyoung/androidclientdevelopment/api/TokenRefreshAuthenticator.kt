package com.nakyoung.androidclientdevelopment.api

import android.util.Log
import com.nakyoung.androidclientdevelopment.api.service.ApiService
import com.nakyoung.androidclientdevelopment.manager.AuthManager
import com.nakyoung.androidclientdevelopment.statics.HEADERS
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

/**
 * Performs either preemptive authentication before connecting to a proxy server,
 * or reactive authentication after receiving a challenge
 * from either an origin web server or proxy server
 * **/
class TokenRefreshAuthenticator: Authenticator{
    override fun authenticate(route: Route?, response: Response): Request? {
        // 앱에서 보낸 요청을 확인하여 원래 보낸 요청의 헤더에 Authorization이 있는지 확인
        val accessToken = response.request.header(HEADERS.AUTHORIZATION)
            ?.split("")
            ?.getOrNull(1)
        // 없으면 Token 관련한 요청이 아니므로 null 반환
        accessToken ?: return null
        // 리프레시 Token이 없으면 토큰 갱신 불가
        AuthManager.refreshToken ?: return null

        val api = ApiService.getInstance()

        // 병렬로 요청을 보냈을 시 여러개의 401 응답을 받게되어 토큰 갱신이 여러번 이루어질 수 있음
        // 이를 막기위해 synchronized 를 통해 동기화처리
        synchronized(this) {
            // accessToken이 이미 갱신된게 아닌지 확인
            // 만약 이미 갱신되었다면 accessToken != AuthManager.accessToken이 된다
            if (accessToken == AuthManager.accessToken) {
                Log.i("TokenRefreshAuthenticator", "갱신할게요")
                val authTokenResponse =
                    api.refreshToken(AuthManager.refreshToken!!)
                        .execute().body()!!
                AuthManager.accessToken = authTokenResponse.accessToken
                AuthManager.refreshToken = authTokenResponse.refreshToken
            }
        }

        // 토큰이 갱신되면 새 Request를 만들고 Authorization 헤더를 변경하여 전송.
        // 로그인이 가능하도록 함
        return response.request.newBuilder()
            .header(HEADERS.AUTHORIZATION, "Bearer ${AuthManager.accessToken}")
            .build()
    }

}
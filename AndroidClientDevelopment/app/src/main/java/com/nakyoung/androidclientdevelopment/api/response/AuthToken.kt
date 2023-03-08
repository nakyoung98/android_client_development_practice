package com.nakyoung.androidclientdevelopment.api.response

/**
 * 토큰 발급/ 갱신 api에서 응답을 받을 때 사용할 객체
 * **/
data class AuthToken(
    val accessToken: String,
    val refreshToken: String,
)
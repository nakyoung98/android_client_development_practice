package com.nakyoung.androidclientdevelopment.api.response

import android.os.Message
import java.util.Date

/**
 * GSON을 초기화한 후 fromJson에 JSon문자열과 .class를 넘겨주면 객체를 만들고
 * 알아서 값을 채워줌
 * **/
data class HelloWorld(val date: Date, val message: String) {
}
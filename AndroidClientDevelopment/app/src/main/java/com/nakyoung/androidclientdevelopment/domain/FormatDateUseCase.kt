package com.nakyoung.androidclientdevelopment.domain

import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.logging.SimpleFormatter

class FormatDateUseCase {
    /**
     * format 패턴 정의
     * **/
    fun formatter(formatPattern: String): SimpleDateFormat {
    return SimpleDateFormat(formatPattern, Locale.KOREA)
    }
}
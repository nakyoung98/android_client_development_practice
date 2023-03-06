package com.nakyoung.androidclientdevelopment.domain

import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.logging.SimpleFormatter

class FormatDateUseCase {
    fun formatter(): SimpleDateFormat {
    return SimpleDateFormat("yy.MM.dd hh:mm:ss", Locale.KOREA)
    }
}
package com.nakyoung.androidclientdevelopment.api.ConverterFactory

import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type
import java.time.LocalDate

class LocalDateConverterFactory: Converter.Factory() {
    /**
     * ApiService.getQuestion()메서드의 인자로 받은 qid:LocalDate를 경로의 문자열로 반환해야함
     * **/
    override fun stringConverter(
        type: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): Converter<*, String>? {
        if (type == LocalDate::class.java) {
            return Converter<LocalDate, String> { it.toString() }
        }
        return null
    }
}
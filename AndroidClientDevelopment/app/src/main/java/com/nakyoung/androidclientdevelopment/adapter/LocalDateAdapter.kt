package com.nakyoung.androidclientdevelopment.adapter

import com.google.gson.*
import java.lang.reflect.Type
import java.time.LocalDate

/**
 * 요청은 LocalDateConverter로 qid를 string으로 변환했지만
 * 응답은 여전히 Gson이 변환하므로 별도의 작업이 필요
 *
 * Gson이 직렬화와 역직렬화로 LocalDate를 처리할 수 있게 하는 Adapter 필요
 * **/
object LocalDateAdapter: JsonSerializer<LocalDate>, JsonDeserializer<LocalDate> {

    override fun serialize(
        src: LocalDate?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        src.toString()
        return JsonPrimitive(src.toString())
    }

    override fun deserialize(
        json: JsonElement,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): LocalDate {
        return LocalDate.parse(json.asString)
    }

}
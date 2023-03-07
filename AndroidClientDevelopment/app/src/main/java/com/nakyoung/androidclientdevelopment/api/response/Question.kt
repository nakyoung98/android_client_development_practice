package com.nakyoung.androidclientdevelopment.api.response

import java.util.*

data class Question(
    val id: String,
    val text: String,
    val updatedAt: Date,
    val createdAt: Date
)

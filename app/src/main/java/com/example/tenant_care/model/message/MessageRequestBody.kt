package com.example.tenant_care.model.message

import kotlinx.serialization.Serializable

@Serializable
data class MessageRequestBody(
    val message: String,
)
@Serializable
data class MessageResponseBody(
    val statusCode: Int,
    val message: String,
    val data: MessageDt
)
@Serializable
data class MessageDt(
    val sms: String
)

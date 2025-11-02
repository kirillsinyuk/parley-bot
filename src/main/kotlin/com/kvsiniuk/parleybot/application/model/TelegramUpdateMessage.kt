package com.kvsiniuk.parleybot.application.model

data class TelegramUpdateMessage(
    val message: String?,
    val chatId: Long,
    val userId: Long,
    val userName: String
)

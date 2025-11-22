package com.kvsiniuk.parleybot.application.model

data class TelegramUpdateMessage(
    val message: String? = null,
    val replyText: String? = null,
    val chatId: Long = 0,
    val userId: Long = 0,
    val userLeftGroup: Boolean = false
)

package com.kvsiniuk.parleybot.port.out

interface TelegramMessagePortOut {
    fun sendMessageByCode(
        chatId: Long,
        msgCode: String,
    )

    fun sendMessage(
        chatId: Long,
        message: String,
    )
}

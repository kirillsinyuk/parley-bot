package com.kvsiniuk.parleybot.port.out

import java.io.File

interface TelegramMessagePortOut {
    fun sendMessageByCode(
        chatId: Long,
        msgCode: String,
    )

    fun sendMessage(
        chatId: Long,
        message: String,
    )

    fun sendVoice(
        chatId: Long,
        voice: File,
    )
}

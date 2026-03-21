package com.kvsiniuk.parleybot.port.output

import java.io.File

interface TelegramMessagePortOut {
    fun sendMessageByCode(
        chatId: Long,
        msgCode: String,
        locale: String,
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

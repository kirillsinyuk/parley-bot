package com.kvsiniuk.parleybot.application.port.out

interface TelegramMessagePort {
    fun sendMessageByCode(
        chatId: Long,
        msgCode: String,
    )

    fun sendDocument(
        chatId: Long,
        photo: ByteArray,
        fileName: String,
    )
}

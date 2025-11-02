package com.kvsiniuk.parleybot.infrastructure.telegram

import com.kvsiniuk.parleybot.infrastructure.MessageSourcePort
import com.kvsiniuk.parleybot.application.port.out.TelegramMessagePort
import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.request.ParseMode
import com.pengrad.telegrambot.request.SendDocument
import com.pengrad.telegrambot.request.SendMessage
import org.springframework.stereotype.Component

@Component
class TelegramMessageAdapter(
    private val bot: TelegramBot,
    private val messagePort: MessageSourcePort,
) : TelegramMessagePort {
    companion object {
        private const val DEFAULT_TYPE = "image/png"
    }

    override fun sendMessageByCode(
        chatId: Long,
        msgCode: String,
    ) {
        val responseMsg = messagePort.getMessage(msgCode)
        mapMessage(chatId, responseMsg)
            .let { bot.execute(it) }
    }

    override fun sendDocument(
        chatId: Long,
        document: ByteArray,
        fileName: String,
    ) {
        mapDocument(chatId, document, fileName)
            .let { bot.execute(it) }
    }

    private fun mapMessage(
        chatId: Long,
        msg: String,
    ) = SendMessage(chatId, msg)
        .also { it.parseMode(ParseMode.HTML) }

    private fun mapDocument(
        chatId: Long,
        document: ByteArray,
        fileName: String,
    ) = SendDocument(chatId, document)
        .contentType(DEFAULT_TYPE)
        .fileName(fileName)
        .also { it.parseMode(ParseMode.HTML) }
}

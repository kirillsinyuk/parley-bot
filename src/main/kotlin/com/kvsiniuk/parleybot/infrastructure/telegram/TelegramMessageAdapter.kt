package com.kvsiniuk.parleybot.infrastructure.telegram

import com.kvsiniuk.parleybot.port.out.MessageSourcePortOut
import com.kvsiniuk.parleybot.port.out.TelegramMessagePortOut
import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.request.ParseMode
import com.pengrad.telegrambot.request.SendMessage
import com.pengrad.telegrambot.request.SendVoice
import org.springframework.stereotype.Component

@Component
class TelegramMessageAdapter(
    private val bot: TelegramBot,
    private val messagePort: MessageSourcePortOut,
) : TelegramMessagePortOut {

    override fun sendMessageByCode(
        chatId: Long,
        msgCode: String,
    ) {
        val responseMsg = messagePort.getMessage(msgCode)
        mapMessage(chatId, responseMsg)
            .let { bot.execute(it) }
    }

    override fun sendMessage(
        chatId: Long,
        message: String,
    ) {
        mapMessage(chatId, message)
            .let { bot.execute(it) }
    }

    override fun sendVoice(chatId: Long, voice: ByteArray) {
        mapVoice(chatId, voice)
            .let { bot.execute(it) }
    }

    private fun mapVoice(
        chatId: Long,
        msg: ByteArray,
    ) = SendVoice(chatId, msg)

    private fun mapMessage(
        chatId: Long,
        msg: String,
    ) = SendMessage(chatId, msg)
        .also { it.parseMode(ParseMode.HTML) }
}

package com.kvsiniuk.parleybot.adapter.telegram.handler.common

import com.kvsiniuk.parleybot.adapter.telegram.handler.TelegramUpdateHandler
import com.kvsiniuk.parleybot.application.model.BotCommand
import com.kvsiniuk.parleybot.application.model.TelegramUpdateMessage
import com.kvsiniuk.parleybot.port.`in`.TranslationProcessingPortIn
import com.kvsiniuk.parleybot.port.`in`.model.GetTranslationsRequest
import com.kvsiniuk.parleybot.port.out.TelegramMessagePortOut
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils

@Component
class TranslateTextHandler(
    private val translationProcessingPortIn: TranslationProcessingPortIn,
    private val telegramMessagePortOut: TelegramMessagePortOut,
) : TelegramUpdateHandler {
    override fun process(update: TelegramUpdateMessage) {
        translationProcessingPortIn.getTranslations(GetTranslationsRequest(update.chatId, update.userId, update.message!!))
            .forEach { message -> telegramMessagePortOut.sendMessage(update.chatId, message)}
    }

    override fun canApply(update: TelegramUpdateMessage) =
        StringUtils.hasText(update.message) && !update.message!!.startsWith(BotCommand.SET_LANG.command)
}

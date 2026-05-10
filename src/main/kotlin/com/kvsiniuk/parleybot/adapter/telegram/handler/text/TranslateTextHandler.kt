package com.kvsiniuk.parleybot.adapter.telegram.handler.text

import com.kvsiniuk.parleybot.adapter.telegram.handler.TelegramUpdateHandler
import com.kvsiniuk.parleybot.application.model.MENU_COMMANDS
import com.kvsiniuk.parleybot.application.model.TelegramUpdateMessage
import com.kvsiniuk.parleybot.port.input.TranslationProcessingPortIn
import com.kvsiniuk.parleybot.port.input.UserPortIn
import com.kvsiniuk.parleybot.port.input.model.GetTranslationsRequest
import com.kvsiniuk.parleybot.port.output.TelegramMessagePortOut
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils

@Component
class TranslateTextHandler(
    private val translationProcessingPortIn: TranslationProcessingPortIn,
    private val userPortIn: UserPortIn,
    private val telegramMessagePortOut: TelegramMessagePortOut,
) : TelegramUpdateHandler {
    override fun process(update: TelegramUpdateMessage) {
        val translations =
            GetTranslationsRequest(update.chatId, update.userId, update.message!!, update.replyText)
                .let { translationProcessingPortIn.getTranslations(it) }
        translations.forEach { telegramMessagePortOut.sendMessage(update.chatId, it) }
        if (translations.isNotEmpty()) {
            userPortIn.incUserMessageCount(update.userId)
        }
    }

    override fun canApply(update: TelegramUpdateMessage) =
        StringUtils.hasText(update.message) && MENU_COMMANDS.none { update.message!!.startsWith(it) }
}

package com.kvsiniuk.parleybot.adapter.telegram.handler.common

import com.kvsiniuk.parleybot.adapter.telegram.handler.TelegramUpdateHandler
import com.kvsiniuk.parleybot.application.model.BotCommand
import com.kvsiniuk.parleybot.application.model.Language
import com.kvsiniuk.parleybot.application.model.TelegramUpdateMessage
import com.kvsiniuk.parleybot.application.model.stringToEnum
import com.kvsiniuk.parleybot.port.`in`.SetUserChatLanguagePortIn
import com.kvsiniuk.parleybot.port.`in`.model.SetLanguageRequest
import com.kvsiniuk.parleybot.port.out.TelegramMessagePortOut
import org.springframework.stereotype.Component

@Component
class SetLanguageCmdHandler(
    private val telegramMessagePort: TelegramMessagePortOut,
    private val setLanguagePortIn: SetUserChatLanguagePortIn,
) : TelegramUpdateHandler {
    override fun process(update: TelegramUpdateMessage) {
        val stringLang = update.message!!.replace("${BotCommand.SET_LANG.command} ", "")
        val language = stringToEnum(stringLang)
        if (language != null) {
            setLanguagePortIn.setLanguage(SetLanguageRequest(update.userId, update.chatId, language))
            telegramMessagePort.sendMessageByCode(update.chatId, "command.set_lang.response")
        } else {
            telegramMessagePort.sendMessageByCode(update.chatId, "Couldn't set language. Valid values: ${Language.values()}")
        }
    }

    override fun canApply(update: TelegramUpdateMessage) =
        update.message?.startsWith(BotCommand.SET_LANG.command) ?: false
}

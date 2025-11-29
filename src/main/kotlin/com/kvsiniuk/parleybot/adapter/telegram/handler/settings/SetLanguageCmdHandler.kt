package com.kvsiniuk.parleybot.adapter.telegram.handler.settings

import com.kvsiniuk.parleybot.adapter.telegram.handler.TelegramUpdateHandler
import com.kvsiniuk.parleybot.application.model.BotCommand
import com.kvsiniuk.parleybot.application.model.Language
import com.kvsiniuk.parleybot.application.model.TelegramUpdateMessage
import com.kvsiniuk.parleybot.application.model.stringToEnum
import com.kvsiniuk.parleybot.port.input.SetUserChatLanguagePortIn
import com.kvsiniuk.parleybot.port.input.model.SetLanguagesRequest
import com.kvsiniuk.parleybot.port.output.TelegramMessagePortOut
import org.springframework.stereotype.Component

@Component
class SetLanguageCmdHandler(
    private val telegramMessagePort: TelegramMessagePortOut,
    private val setLanguagePortIn: SetUserChatLanguagePortIn,
) : TelegramUpdateHandler {
    override fun process(update: TelegramUpdateMessage) {
        val languages = getLanguages(update.message!!)
        if (languages.isNotEmpty()) {
            setLanguagePortIn.setLanguages(SetLanguagesRequest(update.chatId, update.userId, languages))
            telegramMessagePort.sendMessageByCode(update.chatId, "command.set_lang.response")
        } else {
            telegramMessagePort.sendMessageByCode(update.chatId, "Couldn't set language. Valid values: ${Language.values()}")
        }
    }

    private fun getLanguages(message: String): Set<Language> {
        return message
            .replace(BotCommand.SET_LANG.command, "")
            .split(",")
            .mapNotNull { str -> stringToEnum(str.trim()) }
            .toSet()
    }

    override fun canApply(update: TelegramUpdateMessage) = update.message?.startsWith(BotCommand.SET_LANG.command) ?: false
}

package com.kvsiniuk.parleybot.adapter.telegram.handler.settings

import com.kvsiniuk.parleybot.adapter.telegram.handler.AbstractCommandHandler
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
) : AbstractCommandHandler(BotCommand.SET_LANG) {
    override fun process(update: TelegramUpdateMessage) {
        val languages = getLanguages(update.message!!)
        if (languages.isNotEmpty()) {
            setLanguagePortIn.setLanguages(SetLanguagesRequest(update.chatId, update.userId, languages))
            telegramMessagePort.sendMessageByCode(update.chatId, "command.set_lang.response", update.language ?: "en")
        } else {
            telegramMessagePort.sendMessage(update.chatId, "Couldn't set language. Valid values: ${Language.entries}")
        }
    }

    private fun getLanguages(message: String): Set<Language> =
        message
            .replace(BotCommand.SET_LANG.command, "")
            .split(",")
            .mapNotNull { str -> stringToEnum(str.uppercase().trim()) }
            .toSet()
}

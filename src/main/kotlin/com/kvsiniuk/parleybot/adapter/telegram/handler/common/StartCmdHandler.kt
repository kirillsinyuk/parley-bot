package com.kvsiniuk.parleybot.adapter.telegram.handler.common

import com.kvsiniuk.parleybot.adapter.telegram.handler.TelegramUpdateHandler
import com.kvsiniuk.parleybot.application.model.BotCommand
import com.kvsiniuk.parleybot.application.model.TelegramUpdateMessage
import com.kvsiniuk.parleybot.port.output.TelegramMessagePortOut
import org.springframework.stereotype.Component

@Component
class StartCmdHandler(
    private val telegramMessagePort: TelegramMessagePortOut,
) : TelegramUpdateHandler {
    override fun process(update: TelegramUpdateMessage) {
        telegramMessagePort.sendMessageByCode(update.chatId, "command.start.response")
    }

    override fun canApply(update: TelegramUpdateMessage) = update.message?.startsWith(BotCommand.START.command) ?: false
}

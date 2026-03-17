package com.kvsiniuk.parleybot.adapter.telegram.handler.common

import com.kvsiniuk.parleybot.adapter.telegram.handler.AbstractCommandHandler
import com.kvsiniuk.parleybot.application.model.BotCommand
import com.kvsiniuk.parleybot.application.model.TelegramUpdateMessage
import com.kvsiniuk.parleybot.port.output.TelegramMessagePortOut
import org.springframework.stereotype.Component

@Component
class StartCmdHandler(
    private val telegramMessagePort: TelegramMessagePortOut,
) : AbstractCommandHandler(BotCommand.START) {
    override fun process(update: TelegramUpdateMessage) {
        telegramMessagePort.sendMessageByCode(update.chatId, "command.start.response")
    }
}

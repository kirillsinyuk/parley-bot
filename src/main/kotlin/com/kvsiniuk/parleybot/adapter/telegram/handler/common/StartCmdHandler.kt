package com.kvsiniuk.parleybot.adapter.telegram.handler.common

import com.kvsiniuk.parleybot.adapter.telegram.handler.TelegramUpdateHandler
import com.kvsiniuk.parleybot.application.model.BotCommand
import com.kvsiniuk.parleybot.application.model.TelegramUpdateMessage
import com.kvsiniuk.parleybot.application.model.User
import com.kvsiniuk.parleybot.port.`in`.CreateUserPortIn
import com.kvsiniuk.parleybot.port.out.TelegramMessagePortOut
import org.springframework.stereotype.Component

@Component
class StartCmdHandler(
    private val telegramMessagePort: TelegramMessagePortOut,
    private val createUserPortIn: CreateUserPortIn,
) : TelegramUpdateHandler {
    override fun process(update: TelegramUpdateMessage) {
        createUserPortIn.createNewUser(User(update.userId, update.chatId, update.userName))
        telegramMessagePort.sendMessageByCode(update.chatId, "command.start.response")
    }

    override fun canApply(update: TelegramUpdateMessage) = update.message == BotCommand.START.command
}

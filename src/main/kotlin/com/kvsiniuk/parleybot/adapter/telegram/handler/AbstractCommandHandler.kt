package com.kvsiniuk.parleybot.adapter.telegram.handler

import com.kvsiniuk.parleybot.application.model.BotCommand
import com.kvsiniuk.parleybot.application.model.TelegramUpdateMessage

abstract class AbstractCommandHandler(
    private val command: BotCommand,
) : TelegramUpdateHandler {
    override fun canApply(update: TelegramUpdateMessage): Boolean = update.message?.startsWith(command.command) ?: false
}

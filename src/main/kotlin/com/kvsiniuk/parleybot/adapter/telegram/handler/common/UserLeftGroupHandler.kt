package com.kvsiniuk.parleybot.adapter.telegram.handler.common

import com.kvsiniuk.parleybot.adapter.telegram.handler.TelegramUpdateHandler
import com.kvsiniuk.parleybot.application.model.BotCommand
import com.kvsiniuk.parleybot.application.model.TelegramUpdateMessage
import com.kvsiniuk.parleybot.port.input.DeleteUserPortIn
import com.kvsiniuk.parleybot.port.input.model.DeleteUserRequest
import org.springframework.stereotype.Component

@Component
class UserLeftGroupHandler(
    private val deleteUserPortIn: DeleteUserPortIn,
) : TelegramUpdateHandler {
    override fun process(update: TelegramUpdateMessage) {
        deleteUserPortIn.deleteUser(DeleteUserRequest(update.userId))
    }

    override fun canApply(update: TelegramUpdateMessage) =
        update.userLeftGroup || update.message?.equals(
            BotCommand.SET_LANG.command,
        ) ?: false
}

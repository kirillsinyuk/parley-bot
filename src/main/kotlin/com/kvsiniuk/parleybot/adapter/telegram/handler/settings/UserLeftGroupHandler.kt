package com.kvsiniuk.parleybot.adapter.telegram.handler.settings

import com.kvsiniuk.parleybot.adapter.telegram.handler.TelegramUpdateHandler
import com.kvsiniuk.parleybot.application.model.BotCommand
import com.kvsiniuk.parleybot.application.model.TelegramUpdateMessage
import com.kvsiniuk.parleybot.port.input.DeleteUserChatPortIn
import com.kvsiniuk.parleybot.port.input.model.DeleteUserChatRequest
import org.springframework.stereotype.Component

@Component
class UserLeftGroupHandler(
    private val deleteUserChatPortIn: DeleteUserChatPortIn,
) : TelegramUpdateHandler {
    override fun process(update: TelegramUpdateMessage) {
        deleteUserChatPortIn.deleteUserChat(DeleteUserChatRequest(update.userId))
    }

    override fun canApply(update: TelegramUpdateMessage) =
        update.userLeftGroup || update.message?.equals(BotCommand.EXIT.command) ?: false
}

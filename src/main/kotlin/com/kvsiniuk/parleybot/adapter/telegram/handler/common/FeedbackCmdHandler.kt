package com.kvsiniuk.parleybot.adapter.telegram.handler.common

import com.kvsiniuk.parleybot.adapter.telegram.handler.TelegramUpdateHandler
import com.kvsiniuk.parleybot.application.model.BotCommand
import com.kvsiniuk.parleybot.application.model.TelegramUpdateMessage
import com.kvsiniuk.parleybot.config.AdminConfigurationProperties
import com.kvsiniuk.parleybot.port.out.TelegramMessagePortOut
import mu.KLogging
import org.springframework.stereotype.Component

@Component
class FeedbackCmdHandler(
    private val telegramMessagePort: TelegramMessagePortOut,
    private val adminConfigurationProperties: AdminConfigurationProperties,
) : TelegramUpdateHandler {
    override fun process(update: TelegramUpdateMessage) {
        val feedbackMsg = "A feedback from ${update.userId}, chatId ${update.chatId}: ${update.message}. Reply to: ${update.replyText}"
        telegramMessagePort.sendMessage(adminConfigurationProperties.chatId, feedbackMsg)
            .also { logger.warn { feedbackMsg } }
        telegramMessagePort.sendMessageByCode(update.chatId, "command.feedback.response")
    }

    override fun canApply(update: TelegramUpdateMessage) =
        update.message?.startsWith(BotCommand.FEEDBACK.command) ?: false

    companion object : KLogging()
}

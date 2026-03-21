package com.kvsiniuk.parleybot.adapter.telegram.handler.common

import com.kvsiniuk.parleybot.adapter.telegram.handler.AbstractCommandHandler
import com.kvsiniuk.parleybot.application.model.BotCommand
import com.kvsiniuk.parleybot.application.model.TelegramUpdateMessage
import com.kvsiniuk.parleybot.config.AdminConfigurationProperties
import com.kvsiniuk.parleybot.port.output.TelegramMessagePortOut
import mu.KLogging
import org.springframework.stereotype.Component

@Component
class FeedbackCmdHandler(
    private val telegramMessagePort: TelegramMessagePortOut,
    private val adminConfigurationProperties: AdminConfigurationProperties,
) : AbstractCommandHandler(BotCommand.FEEDBACK) {
    override fun process(update: TelegramUpdateMessage) {
        val feedbackMsg = "A feedback from ${update.userId}, chatId ${update.chatId}: ${update.message}. Reply to: ${update.replyText}"
        telegramMessagePort.sendMessage(adminConfigurationProperties.chatId, feedbackMsg)
            .also { logger.warn { feedbackMsg } }
        telegramMessagePort.sendMessageByCode(update.chatId, "command.feedback.response", update.language ?: "en")
    }

    companion object : KLogging()
}

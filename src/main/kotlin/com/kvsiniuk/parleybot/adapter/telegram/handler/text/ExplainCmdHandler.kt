package com.kvsiniuk.parleybot.adapter.telegram.handler.text

import com.kvsiniuk.parleybot.adapter.telegram.handler.TelegramUpdateHandler
import com.kvsiniuk.parleybot.application.model.BotCommand
import com.kvsiniuk.parleybot.application.model.TelegramUpdateMessage
import com.kvsiniuk.parleybot.port.input.ExplainMessagePortIn
import com.kvsiniuk.parleybot.port.input.UserPortIn
import com.kvsiniuk.parleybot.port.output.TelegramMessagePortOut
import io.micrometer.common.util.StringUtils
import org.springframework.stereotype.Component

@Component
class ExplainCmdHandler(
    private val explainMessagePort: ExplainMessagePortIn,
    private val userPortIn: UserPortIn,
    private val telegramMessagePort: TelegramMessagePortOut,
) : TelegramUpdateHandler {
    override fun process(update: TelegramUpdateMessage) {
        update.replyText
            ?.takeIf { StringUtils.isNotBlank(it) }
            ?.let { explainMessagePort.getExplanation(it, update.userId, update.language ?: "en") }
            ?.also { telegramMessagePort.sendMessage(update.chatId, it) }
            ?.also { userPortIn.incUserExplainCount(update.userId) }
            ?: telegramMessagePort.sendMessageByCode(update.chatId, "command.explain.no-text-response")
    }

    override fun canApply(update: TelegramUpdateMessage) = update.message == BotCommand.EXPLAIN.command
}

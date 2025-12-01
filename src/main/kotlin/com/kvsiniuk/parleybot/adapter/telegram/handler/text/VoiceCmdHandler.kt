package com.kvsiniuk.parleybot.adapter.telegram.handler.text

import com.kvsiniuk.parleybot.adapter.telegram.handler.TelegramUpdateHandler
import com.kvsiniuk.parleybot.application.model.BotCommand
import com.kvsiniuk.parleybot.application.model.TelegramUpdateMessage
import com.kvsiniuk.parleybot.port.input.UserPortIn
import com.kvsiniuk.parleybot.port.output.TelegramMessagePortOut
import com.kvsiniuk.parleybot.port.output.TextToSpeechPortOut
import io.micrometer.common.util.StringUtils
import org.springframework.stereotype.Component

@Component
class VoiceCmdHandler(
    private val textToSpeechPort: TextToSpeechPortOut,
    private val userPortIn: UserPortIn,
    private val telegramMessagePort: TelegramMessagePortOut,
) : TelegramUpdateHandler {
    override fun process(update: TelegramUpdateMessage) {
        update.replyText
            ?.substring(0..400)
            ?.takeIf { StringUtils.isNotBlank(it) }
            ?.let { textToSpeechPort.translateToVoice(it) }
            ?.also { telegramMessagePort.sendVoice(update.chatId, it) }
            ?.also { userPortIn.incUserVoiceCount(update.userId) }
            ?: telegramMessagePort.sendMessageByCode(update.chatId, "command.voice.no-text-response")
    }

    override fun canApply(update: TelegramUpdateMessage) = update.message == BotCommand.VOICE.command
}

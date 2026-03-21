package com.kvsiniuk.parleybot.adapter.telegram.handler.text

import com.kvsiniuk.parleybot.adapter.telegram.handler.AbstractCommandHandler
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
) : AbstractCommandHandler(BotCommand.VOICE) {
    override fun process(update: TelegramUpdateMessage) {
        update.replyText
            ?.takeIf { StringUtils.isNotBlank(it) }
            ?.let { textToSpeechPort.translateToVoice(it) }
            ?.also { telegramMessagePort.sendVoice(update.chatId, it) }
            ?.also { userPortIn.incUserVoiceCount(update.userId) }
            ?: telegramMessagePort.sendMessageByCode(update.chatId, "command.voice.no-text-response", update.language ?: "en")
    }
}

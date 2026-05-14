package com.kvsiniuk.parleybot.adapter.telegram.handler.text

import com.kvsiniuk.parleybot.adapter.telegram.handler.TelegramUpdateHandler
import com.kvsiniuk.parleybot.application.model.TelegramUpdateMessage
import com.kvsiniuk.parleybot.port.input.TranslationProcessingPortIn
import com.kvsiniuk.parleybot.port.input.UserPortIn
import com.kvsiniuk.parleybot.port.input.model.GetTranslationsRequest
import com.kvsiniuk.parleybot.port.output.SpeechToTextPortOut
import com.kvsiniuk.parleybot.port.output.TelegramFilePortOut
import com.kvsiniuk.parleybot.port.output.TelegramMessagePortOut
import org.springframework.stereotype.Component

@Component
class VoiceMessageCmdHandler(
    private val telegramFilePort: TelegramFilePortOut,
    private val speechToTextPort: SpeechToTextPortOut,
    private val translationProcessingPort: TranslationProcessingPortIn,
    private val telegramMessagePort: TelegramMessagePortOut,
    private val userPortIn: UserPortIn,
) : TelegramUpdateHandler {
    override fun process(update: TelegramUpdateMessage) {
        val transcribed =
            update.voiceFileId!!
                .let { telegramFilePort.getFileContent(it) }
                .let { speechToTextPort.translateToText(it.file) }
        if (transcribed == null) {
            telegramMessagePort.sendMessageByCode(update.chatId, "command.voice.transcription-error")
            return
        }
        val translations =
            GetTranslationsRequest(update.chatId, update.userId, transcribed, update.replyText)
                .let { translationProcessingPort.getTranslations(it) }
        translations.forEach { telegramMessagePort.sendMessage(update.chatId, it) }
        if (translations.isNotEmpty()) {
            userPortIn.incUserVoiceCount(update.userId)
        }
    }

    override fun canApply(update: TelegramUpdateMessage) = update.voiceFileId != null
}

package com.kvsiniuk.parleybot.infrastructure.voice

import com.kvsiniuk.parleybot.port.output.SpeechToTextPortOut
import org.springframework.ai.audio.transcription.AudioTranscriptionPrompt
import org.springframework.ai.openai.OpenAiAudioTranscriptionModel
import org.springframework.core.io.ByteArrayResource
import org.springframework.retry.annotation.Backoff
import org.springframework.retry.annotation.Retryable
import org.springframework.stereotype.Component

@Component
class SpeechToTextAdapter(
    private val transcriptionModel: OpenAiAudioTranscriptionModel,
) : SpeechToTextPortOut {
    @Retryable(backoff = Backoff(delay = 100, multiplier = 2.0))
    override fun translateToText(file: ByteArray): String? {
        val resource =
            object : ByteArrayResource(file) {
                override fun getFilename(): String = "voice.ogg"
            }
        return transcriptionModel.call(AudioTranscriptionPrompt(resource)).result.output
    }
}

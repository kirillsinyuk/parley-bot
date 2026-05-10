package com.kvsiniuk.parleybot.infrastructure.voice

import com.kvsiniuk.parleybot.port.output.TextToSpeechPortOut
import org.springframework.ai.audio.tts.TextToSpeechPrompt
import org.springframework.ai.openai.OpenAiAudioSpeechModel
import org.springframework.retry.annotation.Backoff
import org.springframework.retry.annotation.Retryable
import org.springframework.stereotype.Component
import java.io.File

@Component
class TextToSpeechAdapter(
    private val speechModel: OpenAiAudioSpeechModel,
) : TextToSpeechPortOut {
    @Retryable(backoff = Backoff(delay = 100, multiplier = 2.0))
    override fun translateToVoice(text: String): File {
        val tempFile = File.createTempFile("upload_", ".mp3")
        try {
            val audio = speechModel.call(TextToSpeechPrompt(text)).result.output
            tempFile.writeBytes(audio)
            return tempFile
        } catch (e: Exception) {
            tempFile.delete()
            throw e
        }
    }
}

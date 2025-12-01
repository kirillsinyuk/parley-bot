package com.kvsiniuk.parleybot.infrastructure.voice

import com.kvsiniuk.parleybot.port.output.SpeechToTextPortOut
import com.openai.client.OpenAIClient
import com.openai.models.audio.AudioModel
import com.openai.models.audio.transcriptions.TranscriptionCreateParams
import mu.KLogging
import org.springframework.retry.annotation.Backoff
import org.springframework.retry.annotation.Retryable
import org.springframework.stereotype.Component
import java.nio.file.Files

@Component
class SpeechToTextAdapter(
    private val openaiClient: OpenAIClient,
) : SpeechToTextPortOut {
    @Retryable(backoff = Backoff(delay = 100, multiplier = 2.0))
    override fun translateToText(file: ByteArray): String? = openaiClientCall(file)

    private fun openaiClientCall(file: ByteArray): String? {
        val tempFile = Files.createTempFile("voice", ".ogg")
        Files.write(tempFile, file)
        var param =
            TranscriptionCreateParams.builder()
                .file(tempFile)
                .model(AudioModel.GPT_4O_MINI_TRANSCRIBE)
                .build()
        return openaiClient.audio().transcriptions().create(param)
            .transcription()
            .map { it.text() }
            .orElse(null)
    }

    companion object : KLogging()
}

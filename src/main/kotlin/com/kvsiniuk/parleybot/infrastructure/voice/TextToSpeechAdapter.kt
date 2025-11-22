package com.kvsiniuk.parleybot.infrastructure.voice

import com.kvsiniuk.parleybot.port.out.TextToSpeechPortOut
import com.openai.client.OpenAIClient
import com.openai.models.audio.speech.SpeechCreateParams
import com.openai.models.audio.speech.SpeechModel
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import mu.KLogging
import org.springframework.retry.annotation.Backoff
import org.springframework.retry.annotation.Retryable
import org.springframework.stereotype.Component

@Component
class TextToSpeechAdapter(
	private val openaiClient: OpenAIClient,
) : TextToSpeechPortOut {

	private final val SYSTEM_PROMPT = "Speak in a neutral and positive tone."

	@Retryable(backoff = Backoff(delay = 100, multiplier = 2.0))
	override fun translateToVoice(text: String): File {
		val tempFile = File.createTempFile("upload_", ".mp3")
		openaiClientCall(text).use { it.copyTo(tempFile.outputStream()) }
		return tempFile
	}

	private fun openaiClientCall(sourceText: String): InputStream {
		val params = SpeechCreateParams.builder()
			.model(SpeechModel.GPT_4O_MINI_TTS)
			.input(sourceText)
			.voice(SpeechCreateParams.Voice.ALLOY)
			.instructions(SYSTEM_PROMPT)
			.build()
		return openaiClient.audio().speech().create(params)
			.body()
	}

	companion object : KLogging()
}
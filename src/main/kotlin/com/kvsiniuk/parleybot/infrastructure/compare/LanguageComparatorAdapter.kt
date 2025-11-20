package com.kvsiniuk.parleybot.infrastructure.compare

import com.fasterxml.jackson.databind.ObjectMapper
import com.kvsiniuk.parleybot.port.out.LanguageComparatorPortOut
import com.openai.client.OpenAIClient
import com.openai.models.ChatModel
import com.openai.models.responses.EasyInputMessage
import com.openai.models.responses.ResponseCreateParams
import com.openai.models.responses.ResponseInputItem
import mu.KLogging
import org.springframework.retry.annotation.Backoff
import org.springframework.retry.annotation.Retryable
import org.springframework.stereotype.Component

@Component
class LanguageComparatorAdapter(
	private val openaiClient: OpenAIClient,
	private val objectMapper: ObjectMapper,
) : LanguageComparatorPortOut {

	private final val SYSTEM_PROMPT = """
		You are a language comparator.

		## OBJECTIVE
		Compare languages of source and target texts.
		
		## RULES
		1. You are given 2 texts: source and target. If languages in these texts are identical, return one word 'true'.
		Otherwise return 'false'.
		2. Some distinct words in different language are possible. It must not impact an overall result.
		3. No explanations, no extra fields, no surrounding text. Only true of false.
		
		## OUTPUT FORMAT (MANDATORY)
		You MUST return ONLY true of false
	"""

	@Retryable(backoff = Backoff(delay = 100, multiplier = 2.0))
	override fun haveSameLanguage(sourceText: String, translatedText: String): Boolean {
		logger.info("Processing text comparison. Source=$sourceText. Target=$translatedText")
		return mapToObject(openaiClientCall(sourceText, translatedText))
			.also { logger.info { "Comparison result: $it" } }
	}

	private fun openaiClientCall(sourceText: String, translatedText: String): String {
		val params = ResponseCreateParams.builder()
			.inputOfResponse(
				listOf(
					ResponseInputItem.ofEasyInputMessage(
						EasyInputMessage.builder()
							.role(EasyInputMessage.Role.SYSTEM)
							.content(SYSTEM_PROMPT)
							.build()
					),
					ResponseInputItem.ofEasyInputMessage(
						EasyInputMessage.builder()
							.role(EasyInputMessage.Role.USER)
							.content("{ sourceText=$sourceText, targetText=$translatedText }")
							.build()
					)
				)
			)
			.model(ChatModel.GPT_4_1_NANO)
			.build()
		return openaiClient.responses().create(params)
			.output()[0]
			.asMessage()
			.content()[0]
			.asOutputText()
			.text()
	}

	private fun mapToObject(rawResult: String): Boolean = try {
		objectMapper.readValue(rawResult, Boolean::class.java)
	} catch (e: RuntimeException) {
		logger.error { "Exception during parsing comparison result: $rawResult" }
		false
	}

	companion object : KLogging()
}
package com.kvsiniuk.parleybot.infrastructure.comparator

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
		Determine whether the sourceText are PRIMARILY written in the targetLanguage.

		## DEFINITIONS
		PRIMARILY = the language used for the majority of meaningful words in a text.

		Mixed-language text is common. English technical terms, code words, proper names, UI labels, or loanwords inside another language do NOT change the primary language.

		Examples:
		- "выглядит как feature-request" → russian
		- "Install драйвер" → russian
		- "I need to fix комп" → english

		## RULES
		1. Identify the primary language of the sourceText.
		2. If the primary language matches targetLanguage → output true.
		3. If they differ → output false.
		4. Ignore:
		   - individual foreign words
		   - English technical terms widely used in other languages (feature, bug, task, commit, request…)
		   - code tokens or identifiers
		   - transliterations
		   - names, brand names, URLs
		5. Consider the writing system (script) only as an additional clue, NOT the main rule.
		6. Output ONLY: true or false. No quotes. No extra text.
	"""

	@Retryable(backoff = Backoff(delay = 100, multiplier = 2.0))
	override fun haveSameLanguage(sourceText: String, targetLanguage: String): Boolean {
		logger.info("Processing text comparison. Source=$sourceText. TargetLanguage=$targetLanguage")
		return mapToObject(openaiClientCall(sourceText, targetLanguage))
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
							.content("{ sourceText=$sourceText, targetLanguage=$translatedText }")
							.build()
					)
				)
			)
			.model(ChatModel.GPT_5_NANO)
			.build()
		return openaiClient.responses().create(params)
			.output()
			.first { it.isMessage() }
			.asMessage()
			.content()
			.first { it.isOutputText() }
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
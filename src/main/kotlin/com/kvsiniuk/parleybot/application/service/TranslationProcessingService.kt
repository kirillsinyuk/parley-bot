package com.kvsiniuk.parleybot.application.service

import com.kvsiniuk.parleybot.infrastructure.database.UserRepository
import com.kvsiniuk.parleybot.port.`in`.TranslationProcessingPortIn
import com.kvsiniuk.parleybot.port.`in`.model.GetTranslationsRequest
import com.kvsiniuk.parleybot.port.out.LanguageComparatorPortOut
import com.kvsiniuk.parleybot.port.out.TranslationPortOut
import mu.KLogging
import org.springframework.stereotype.Component

@Component
class TranslationProcessingService(
	private val userRepository: UserRepository,
	private val translateService: TranslationPortOut,
	private val languageComparator: LanguageComparatorPortOut,
) : TranslationProcessingPortIn {
	override fun getTranslations(request: GetTranslationsRequest): List<String> =
		getChatLanguages(request)
			.mapNotNull { getChatLanguages(request.message, it.languageName) }
			.filter { !languageComparator.haveSameLanguage(request.message, it) }

	private fun getChatLanguages(request: GetTranslationsRequest) =
		userRepository.findAllByChatId(request.chatId)
			.filter { it.userId != request.userId }
			.map { it.language }
			.distinct()

	private fun getChatLanguages(message: String, language: String) =
		try {
			translateService.translate(message, language)
		} catch (e: RuntimeException) {
			logger.error("Error during translation to $language: $message", e)
			null
		}

	companion object : KLogging()
}
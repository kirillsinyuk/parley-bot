package com.kvsiniuk.parleybot.application.service

import com.kvsiniuk.parleybot.infrastructure.database.UserRepository
import com.kvsiniuk.parleybot.port.`in`.TranslationProcessingPortIn
import com.kvsiniuk.parleybot.port.`in`.model.GetTranslationsRequest
import org.springframework.stereotype.Component

@Component
class TranslationProcessingService(
	private val userRepository: UserRepository,
	private val translateService: TranslateService,
): TranslationProcessingPortIn {
	override fun getTranslations(request: GetTranslationsRequest): List<String> =
		getChatLanguages(request)
			.map { language -> translateService.translate(request.message, language) }

	private fun getChatLanguages(request: GetTranslationsRequest) =
		userRepository.findAllByChatId(request.chatId)
			.filter { user -> user.userId != request.userId }
			.map { user -> user.language }
			.distinct()
}
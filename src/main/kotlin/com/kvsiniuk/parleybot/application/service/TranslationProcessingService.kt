package com.kvsiniuk.parleybot.application.service

import com.kvsiniuk.parleybot.infrastructure.database.UserRepository
import com.kvsiniuk.parleybot.port.`in`.TranslationProcessingPortIn
import com.kvsiniuk.parleybot.port.`in`.model.GetTranslationsRequest
import com.kvsiniuk.parleybot.port.out.TranslationPortOut
import org.springframework.stereotype.Component

@Component
class TranslationProcessingService(
	private val userRepository: UserRepository,
	private val translateService: TranslationPortOut,
): TranslationProcessingPortIn {
	override fun getTranslations(request: GetTranslationsRequest): List<String> =
		getChatLanguages(request).mapNotNull { language ->
			translateService.translate(
				request.message,
				language.languageName
			)
		}

	private fun getChatLanguages(request: GetTranslationsRequest) =
		userRepository.findAllByChatId(request.chatId)
			.filter { user -> user.userId != request.userId }
			.map { user -> user.language }
			.distinct()
}
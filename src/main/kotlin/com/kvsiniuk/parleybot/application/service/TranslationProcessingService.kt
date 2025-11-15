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
		userRepository.findAllByChatId(request.chatId).stream()
			.filter { user -> user.userId != request.userId }
			.map { user -> translateService.translate(request.message, user.language) }
			.toList()
}
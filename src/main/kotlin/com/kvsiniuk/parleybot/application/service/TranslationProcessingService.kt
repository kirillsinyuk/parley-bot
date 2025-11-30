package com.kvsiniuk.parleybot.application.service

import com.kvsiniuk.parleybot.infrastructure.database.UserChatRepository
import com.kvsiniuk.parleybot.port.input.TranslationProcessingPortIn
import com.kvsiniuk.parleybot.port.input.model.GetTranslationsRequest
import com.kvsiniuk.parleybot.port.output.LanguageComparatorPortOut
import com.kvsiniuk.parleybot.port.output.TranslationPortOut
import mu.KLogging
import org.springframework.stereotype.Component

@Component
class TranslationProcessingService(
    private val userChatRepository: UserChatRepository,
    private val translateService: TranslationPortOut,
    private val languageComparator: LanguageComparatorPortOut,
) : TranslationProcessingPortIn {
    override fun getTranslations(request: GetTranslationsRequest): List<String> =
        getChatLanguages(request)
            .mapNotNull { translateText(request.message, it.languageName, request.replyTo) }
            .filter { languageComparator.wasTranslated(request.message, it) }

    private fun getChatLanguages(request: GetTranslationsRequest) =
        userChatRepository.findAllByChatId(request.chatId)
            .filter { it.userId != request.userId }
            .flatMap { it.languages }
            .distinct()

    private fun translateText(
        message: String,
        language: String,
        replyTo: String?,
    ) = try {
        translateService.translate(message, language, replyTo)
    } catch (e: RuntimeException) {
        logger.error("Error during translation to $language: $message", e)
        null
    }

    companion object : KLogging()
}

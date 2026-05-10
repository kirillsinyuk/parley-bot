package com.kvsiniuk.parleybot.application.service

import com.kvsiniuk.parleybot.port.input.TranslationProcessingPortIn
import com.kvsiniuk.parleybot.port.input.model.GetTranslationsRequest
import com.kvsiniuk.parleybot.port.output.LanguageComparatorPortOut
import com.kvsiniuk.parleybot.port.output.TranslationPortOut
import com.kvsiniuk.parleybot.port.output.UserChatPortOut
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Component

private val logger = KotlinLogging.logger {}

@Component
class TranslationProcessingService(
    private val userChatPortOut: UserChatPortOut,
    private val translateService: TranslationPortOut,
    private val languageComparator: LanguageComparatorPortOut,
) : TranslationProcessingPortIn {
    override fun getTranslations(request: GetTranslationsRequest): List<String> =
        userChatPortOut
            .findLanguagesForChat(request.chatId, request.userId)
            .mapNotNull { translateText(request.message, it.languageName, request.replyTo) }
            .filter { languageComparator.wasTranslated(request.message, it) }

    private fun translateText(
        message: String,
        language: String,
        replyTo: String?,
    ) = try {
        translateService.translate(message, language, replyTo)
    } catch (e: RuntimeException) {
        logger.error(e) { "Error during translation to $language: $message" }
        null
    }
}

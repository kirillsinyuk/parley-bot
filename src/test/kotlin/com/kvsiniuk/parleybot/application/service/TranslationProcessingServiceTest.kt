package com.kvsiniuk.parleybot.application.service

import com.kvsiniuk.parleybot.application.model.Language
import com.kvsiniuk.parleybot.port.input.model.GetTranslationsRequest
import com.kvsiniuk.parleybot.port.output.LanguageComparatorPortOut
import com.kvsiniuk.parleybot.port.output.TranslationPortOut
import com.kvsiniuk.parleybot.port.output.UserChatPortOut
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class TranslationProcessingServiceTest {

    private val userChatPortOut = mockk<UserChatPortOut>()
    private val translateService = mockk<TranslationPortOut>()
    private val languageComparator = mockk<LanguageComparatorPortOut>()

    private val service = TranslationProcessingService(userChatPortOut, translateService, languageComparator)

    @Test
    fun `returns empty list when no other users are in chat`() {
        every { userChatPortOut.findLanguagesForChat(CHAT_ID, SENDER_ID) } returns emptyList()

        val result = service.getTranslations(request("hello"))

        assertTrue(result.isEmpty())
    }

    @Test
    fun `returns translation for each distinct target language`() {
        every { userChatPortOut.findLanguagesForChat(CHAT_ID, SENDER_ID) } returns listOf(Language.ES, Language.DE)
        every { translateService.translate("hello", "spanish", null) } returns "hola"
        every { translateService.translate("hello", "deutsch", null) } returns "hallo"
        every { languageComparator.wasTranslated("hello", "hola") } returns true
        every { languageComparator.wasTranslated("hello", "hallo") } returns true

        val result = service.getTranslations(request("hello"))

        assertEquals(setOf("hola", "hallo"), result.toSet())
    }

    @Test
    fun `filters out result when comparator says no translation occurred`() {
        every { userChatPortOut.findLanguagesForChat(CHAT_ID, SENDER_ID) } returns listOf(Language.ES)
        every { translateService.translate("hello", "spanish", null) } returns "hello"
        every { languageComparator.wasTranslated("hello", "hello") } returns false

        val result = service.getTranslations(request("hello"))

        assertTrue(result.isEmpty())
    }

    @Test
    fun `skips failed language but continues with remaining languages`() {
        every { userChatPortOut.findLanguagesForChat(CHAT_ID, SENDER_ID) } returns listOf(Language.ES, Language.DE)
        every { translateService.translate("hello", "spanish", null) } throws RuntimeException("OpenAI timeout")
        every { translateService.translate("hello", "deutsch", null) } returns "hallo"
        every { languageComparator.wasTranslated("hello", "hallo") } returns true

        val result = service.getTranslations(request("hello"))

        assertEquals(listOf("hallo"), result)
    }

    @Test
    fun `forwards reply context to the translation service`() {
        every { userChatPortOut.findLanguagesForChat(CHAT_ID, SENDER_ID) } returns listOf(Language.ES)
        every { translateService.translate("yes", "spanish", "are you coming?") } returns "sí"
        every { languageComparator.wasTranslated("yes", "sí") } returns true

        service.getTranslations(request("yes", replyTo = "are you coming?"))

        verify(exactly = 1) { translateService.translate("yes", "spanish", "are you coming?") }
    }

    @Test
    fun `translates once per language returned by the port`() {
        // Deduplication is the port's responsibility; the service translates each entry it receives
        every { userChatPortOut.findLanguagesForChat(CHAT_ID, SENDER_ID) } returns listOf(Language.ES)
        every { translateService.translate("hello", "spanish", null) } returns "hola"
        every { languageComparator.wasTranslated("hello", "hola") } returns true

        service.getTranslations(request("hello"))

        verify(exactly = 1) { translateService.translate("hello", "spanish", null) }
    }

    // ── helpers ────────────────────────────────────────────────────────────

    private fun request(message: String, replyTo: String? = null) =
        GetTranslationsRequest(chatId = CHAT_ID, userId = SENDER_ID, message = message, replyTo = replyTo)

    companion object {
        private const val CHAT_ID = 100L
        private const val SENDER_ID = 42L
    }
}

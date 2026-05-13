package com.kvsiniuk.parleybot.application.service

import com.kvsiniuk.parleybot.application.model.Language
import com.kvsiniuk.parleybot.port.input.model.GetTranslationsRequest
import com.kvsiniuk.parleybot.port.output.ChatHistoryPortOut
import com.kvsiniuk.parleybot.port.output.LanguageComparatorPortOut
import com.kvsiniuk.parleybot.port.output.TranslationPortOut
import com.kvsiniuk.parleybot.port.output.UserChatPortOut
import com.kvsiniuk.parleybot.port.output.model.TranslationContext
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class TranslationProcessingServiceTest {
    private val userChatPortOut = mockk<UserChatPortOut>()
    private val translateService = mockk<TranslationPortOut>()
    private val languageComparator = mockk<LanguageComparatorPortOut>()
    private val chatHistory =
        mockk<ChatHistoryPortOut> {
            every { recent(any()) } returns emptyList()
            every { record(any(), any()) } just Runs
        }

    private val service =
        TranslationProcessingService(userChatPortOut, translateService, languageComparator, chatHistory)

    @Test
    fun `returns empty list when no other users are in chat`() {
        every { userChatPortOut.findLanguagesForChat(CHAT_ID, SENDER_ID) } returns emptyList()

        val result = service.getTranslations(request("hello"))

        assertTrue(result.isEmpty())
    }

    @Test
    fun `returns translation for each distinct target language`() {
        every { userChatPortOut.findLanguagesForChat(CHAT_ID, SENDER_ID) } returns listOf(Language.ES, Language.DE)
        every { translateService.translate("hello", "spanish", TranslationContext()) } returns "hola"
        every { translateService.translate("hello", "deutsch", TranslationContext()) } returns "hallo"
        every { languageComparator.wasTranslated("hello", "hola") } returns true
        every { languageComparator.wasTranslated("hello", "hallo") } returns true

        val result = service.getTranslations(request("hello"))

        assertEquals(setOf("hola", "hallo"), result.toSet())
    }

    @Test
    fun `filters out result when comparator says no translation occurred`() {
        every { userChatPortOut.findLanguagesForChat(CHAT_ID, SENDER_ID) } returns listOf(Language.ES)
        every { translateService.translate("hello", "spanish", TranslationContext()) } returns "hello"
        every { languageComparator.wasTranslated("hello", "hello") } returns false

        val result = service.getTranslations(request("hello"))

        assertTrue(result.isEmpty())
    }

    @Test
    fun `skips failed language but continues with remaining languages`() {
        every { userChatPortOut.findLanguagesForChat(CHAT_ID, SENDER_ID) } returns listOf(Language.ES, Language.DE)
        every {
            translateService.translate("hello", "spanish", TranslationContext())
        } throws RuntimeException("OpenAI timeout")
        every { translateService.translate("hello", "deutsch", TranslationContext()) } returns "hallo"
        every { languageComparator.wasTranslated("hello", "hallo") } returns true

        val result = service.getTranslations(request("hello"))

        assertEquals(listOf("hallo"), result)
    }

    @Test
    fun `forwards reply context to the translation service`() {
        val expectedContext = TranslationContext(replyTo = "are you coming?")
        every { userChatPortOut.findLanguagesForChat(CHAT_ID, SENDER_ID) } returns listOf(Language.ES)
        every { translateService.translate("yes", "spanish", expectedContext) } returns "sí"
        every { languageComparator.wasTranslated("yes", "sí") } returns true

        service.getTranslations(request("yes", replyTo = "are you coming?"))

        verify(exactly = 1) { translateService.translate("yes", "spanish", expectedContext) }
    }

    @Test
    fun `passes recent chat history as context when present`() {
        every { chatHistory.recent(CHAT_ID) } returns listOf("привет", "как дела")
        val expectedContext = TranslationContext(recentMessages = listOf("привет", "как дела"))
        every { userChatPortOut.findLanguagesForChat(CHAT_ID, SENDER_ID) } returns listOf(Language.EN)
        every { translateService.translate("норм", "english", expectedContext) } returns "fine"
        every { languageComparator.wasTranslated("норм", "fine") } returns true

        service.getTranslations(request("норм"))

        verify(exactly = 1) { translateService.translate("норм", "english", expectedContext) }
    }

    @Test
    fun `combines recent history and reply context`() {
        every { chatHistory.recent(CHAT_ID) } returns listOf("are you coming?")
        val expectedContext =
            TranslationContext(
                recentMessages = listOf("are you coming?"),
                replyTo = "are you coming?",
            )
        every { userChatPortOut.findLanguagesForChat(CHAT_ID, SENDER_ID) } returns listOf(Language.ES)
        every { translateService.translate("yes", "spanish", expectedContext) } returns "sí"
        every { languageComparator.wasTranslated("yes", "sí") } returns true

        service.getTranslations(request("yes", replyTo = "are you coming?"))

        verify(exactly = 1) { translateService.translate("yes", "spanish", expectedContext) }
    }

    @Test
    fun `records translatable message into chat history`() {
        every { userChatPortOut.findLanguagesForChat(CHAT_ID, SENDER_ID) } returns listOf(Language.ES)
        every { translateService.translate("hello", "spanish", TranslationContext()) } returns "hola"
        every { languageComparator.wasTranslated("hello", "hola") } returns true

        service.getTranslations(request("hello"))

        verify(exactly = 1) { chatHistory.record(CHAT_ID, "hello") }
    }

    @Test
    fun `excludes current message from its own context`() {
        // recent() must be queried before record() so the current message is not part of its own context
        every { userChatPortOut.findLanguagesForChat(CHAT_ID, SENDER_ID) } returns listOf(Language.ES)
        every { translateService.translate("hello", "spanish", TranslationContext()) } returns "hola"
        every { languageComparator.wasTranslated("hello", "hola") } returns true

        service.getTranslations(request("hello"))

        io.mockk.verifyOrder {
            chatHistory.recent(CHAT_ID)
            chatHistory.record(CHAT_ID, "hello")
        }
    }

    @Test
    fun `translates once per language returned by the port`() {
        // Deduplication is the port's responsibility; the service translates each entry it receives
        every { userChatPortOut.findLanguagesForChat(CHAT_ID, SENDER_ID) } returns listOf(Language.ES)
        every { translateService.translate("hello", "spanish", TranslationContext()) } returns "hola"
        every { languageComparator.wasTranslated("hello", "hola") } returns true

        service.getTranslations(request("hello"))

        verify(exactly = 1) { translateService.translate("hello", "spanish", TranslationContext()) }
    }

    @ParameterizedTest
    @ValueSource(
        strings = [
            "",
            "   ",
            "...",
            "!!!",
            ",,, ...",
            "?!.,;:",
            "😀",
            "😀😀😀",
            "😀 🎉 🚀",
            "!!! 😀 ???",
            "123",
            "42 + 7 = 49",
            "100%",
            "$1,000.00",
            "+1 (555) 123-4567",
            "—",
            "🇺🇸 🇩🇪",
        ],
    )
    fun `returns empty list and skips translation when message has no letters`(message: String) {
        val result = service.getTranslations(request(message))

        assertTrue(result.isEmpty())
        verify(exactly = 0) { userChatPortOut.findLanguagesForChat(any(), any()) }
        verify(exactly = 0) { translateService.translate(any(), any(), any()) }
        verify(exactly = 0) { languageComparator.wasTranslated(any(), any()) }
        verify(exactly = 0) { chatHistory.record(any(), any()) }
    }

    @Test
    fun `translates when message mixes a letter with punctuation and emoji`() {
        every { userChatPortOut.findLanguagesForChat(CHAT_ID, SENDER_ID) } returns listOf(Language.ES)
        every { translateService.translate("ok! 😀", "spanish", TranslationContext()) } returns "¡bien! 😀"
        every { languageComparator.wasTranslated("ok! 😀", "¡bien! 😀") } returns true

        val result = service.getTranslations(request("ok! 😀"))

        assertEquals(listOf("¡bien! 😀"), result)
    }

    // ── helpers ────────────────────────────────────────────────────────────

    private fun request(
        message: String,
        replyTo: String? = null,
    ) = GetTranslationsRequest(chatId = CHAT_ID, userId = SENDER_ID, message = message, replyTo = replyTo)

    companion object {
        private const val CHAT_ID = 100L
        private const val SENDER_ID = 42L
    }
}

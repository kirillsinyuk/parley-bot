package com.kvsiniuk.parleybot.adapter.telegram.handler.settings

import com.kvsiniuk.parleybot.application.model.Language
import com.kvsiniuk.parleybot.application.model.TelegramUpdateMessage
import com.kvsiniuk.parleybot.port.input.SetUserChatLanguagePortIn
import com.kvsiniuk.parleybot.port.input.model.SetLanguagesRequest
import com.kvsiniuk.parleybot.port.output.TelegramMessagePortOut
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SetLanguageCmdHandlerTest {

    private val telegramMessagePort = mockk<TelegramMessagePortOut>(relaxed = true)
    private val setLanguagePortIn = mockk<SetUserChatLanguagePortIn>(relaxed = true)
    private val handler = SetLanguageCmdHandler(telegramMessagePort, setLanguagePortIn)

    // ── canApply ───────────────────────────────────────────────────────────

    @Test
    fun `applies to messages starting with the lang command`() {
        assertTrue(handler.canApply(update("/lang EN")))
    }

    @Test
    fun `does not apply to other commands`() {
        assertFalse(handler.canApply(update("/start")))
        assertFalse(handler.canApply(update("/help")))
        assertFalse(handler.canApply(update("hello")))
    }

    // ── language parsing ───────────────────────────────────────────────────

    @Test
    fun `parses a single valid language code`() {
        handler.process(update("/lang EN"))

        verify { setLanguagePortIn.setLanguages(SetLanguagesRequest(CHAT_ID, USER_ID, setOf(Language.EN))) }
    }

    @Test
    fun `parses multiple comma-separated language codes`() {
        handler.process(update("/lang EN,ES,DE"))

        val slot = slot<SetLanguagesRequest>()
        verify { setLanguagePortIn.setLanguages(capture(slot)) }
        assertEquals(setOf(Language.EN, Language.ES, Language.DE), slot.captured.languages)
    }

    @Test
    fun `parsing is case-insensitive`() {
        handler.process(update("/lang en"))

        verify { setLanguagePortIn.setLanguages(SetLanguagesRequest(CHAT_ID, USER_ID, setOf(Language.EN))) }
    }

    @Test
    fun `trims whitespace around language codes`() {
        handler.process(update("/lang  EN , ES "))

        val slot = slot<SetLanguagesRequest>()
        verify { setLanguagePortIn.setLanguages(capture(slot)) }
        assertEquals(setOf(Language.EN, Language.ES), slot.captured.languages)
    }

    @Test
    fun `sends an error message and skips saving when all codes are invalid`() {
        handler.process(update("/lang KLINGON"))

        verify(exactly = 0) { setLanguagePortIn.setLanguages(any()) }
        verify { telegramMessagePort.sendMessage(CHAT_ID, any()) }
    }

    @Test
    fun `saves only valid codes from a mixed valid and invalid list`() {
        handler.process(update("/lang EN,INVALID,ES"))

        val slot = slot<SetLanguagesRequest>()
        verify { setLanguagePortIn.setLanguages(capture(slot)) }
        assertEquals(setOf(Language.EN, Language.ES), slot.captured.languages)
    }

    @Test
    fun `sends confirmation message after successfully setting languages`() {
        handler.process(update("/lang RU"))

        verify { telegramMessagePort.sendMessageByCode(CHAT_ID, "command.set_lang.response") }
    }

    // ── helpers ────────────────────────────────────────────────────────────

    private fun update(message: String) = TelegramUpdateMessage(
        message = message,
        chatId = CHAT_ID,
        userId = USER_ID,
        language = null,
        voiceFileId = null,
    )

    companion object {
        private const val CHAT_ID = 10L
        private const val USER_ID = 42L
    }
}

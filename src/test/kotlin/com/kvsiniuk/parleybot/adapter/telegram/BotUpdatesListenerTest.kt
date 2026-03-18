package com.kvsiniuk.parleybot.adapter.telegram

import com.kvsiniuk.parleybot.adapter.mapper.TelegramUpdateMessageMapper
import com.kvsiniuk.parleybot.adapter.telegram.handler.TelegramUpdateHandler
import com.kvsiniuk.parleybot.application.model.TelegramUpdateMessage
import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.UpdatesListener
import com.pengrad.telegrambot.model.Update
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class BotUpdatesListenerTest {

    private val bot = mockk<TelegramBot>(relaxed = true)
    private val mapper = mockk<TelegramUpdateMessageMapper>()
    private val handlerA = mockk<TelegramUpdateHandler>()
    private val handlerB = mockk<TelegramUpdateHandler>()

    // Instantiate directly — @PostConstruct (bot.setUpdatesListener) is not called outside Spring
    private val listener = BotUpdatesListener(bot, listOf(handlerA, handlerB), mapper)

    private val update1 = mockk<Update>()
    private val update2 = mockk<Update>()
    private val msg1 = msg(chatId = 1L)
    private val msg2 = msg(chatId = 2L)

    @BeforeEach
    fun setup() {
        every { mapper.toMessage(update1) } returns msg1
        every { mapper.toMessage(update2) } returns msg2
        every { handlerA.canApply(any()) } returns false
        every { handlerB.canApply(any()) } returns false
    }

    @Test
    fun `exception in handler for one update does not block processing of the next update`() {
        every { handlerA.canApply(msg1) } returns true
        every { handlerA.process(msg1) } throws RuntimeException("handler exploded")
        every { handlerA.canApply(msg2) } returns true
        every { handlerA.process(msg2) } just runs

        listener.process(mutableListOf(update1, update2))

        verify { handlerA.process(msg2) }
    }

    @Test
    fun `returns CONFIRMED_UPDATES_ALL even when a handler throws`() {
        every { handlerA.canApply(msg1) } returns true
        every { handlerA.process(msg1) } throws RuntimeException("handler exploded")

        val result = listener.process(mutableListOf(update1))

        assertEquals(UpdatesListener.CONFIRMED_UPDATES_ALL, result)
    }

    @Test
    fun `handles null updates list without throwing`() {
        val result = listener.process(null)

        assertEquals(UpdatesListener.CONFIRMED_UPDATES_ALL, result)
    }

    @Test
    fun `only calls handlers whose canApply returns true`() {
        every { handlerA.canApply(msg1) } returns false
        every { handlerB.canApply(msg1) } returns true
        every { handlerB.process(msg1) } just runs

        listener.process(mutableListOf(update1))

        verify(exactly = 0) { handlerA.process(any()) }
        verify(exactly = 1) { handlerB.process(msg1) }
    }

    @Test
    fun `calls all matching handlers for the same update`() {
        every { handlerA.canApply(msg1) } returns true
        every { handlerA.process(msg1) } just runs
        every { handlerB.canApply(msg1) } returns true
        every { handlerB.process(msg1) } just runs

        listener.process(mutableListOf(update1))

        verify { handlerA.process(msg1) }
        verify { handlerB.process(msg1) }
    }

    // ── helpers ────────────────────────────────────────────────────────────

    private fun msg(chatId: Long = 1L) =
        TelegramUpdateMessage(chatId = chatId, userId = 1L, language = null, voiceFileId = null)
}

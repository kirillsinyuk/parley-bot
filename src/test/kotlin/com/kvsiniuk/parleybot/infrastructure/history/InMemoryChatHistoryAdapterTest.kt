package com.kvsiniuk.parleybot.infrastructure.history

import org.junit.jupiter.api.Test
import org.springframework.ai.chat.memory.MessageWindowChatMemory
import java.time.Instant
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class InMemoryChatHistoryAdapterTest {
    private fun adapter(maxMessages: Int = 3) =
        InMemoryChatHistoryAdapter(
            MessageWindowChatMemory.builder().maxMessages(maxMessages).build(),
        )

    @Test
    fun `returns empty list when no messages recorded`() {
        val sut = adapter()

        assertTrue(sut.recent(CHAT_ID).isEmpty())
    }

    @Test
    fun `recent returns messages in insertion order`() {
        val sut = adapter()

        sut.record(CHAT_ID, "one")
        sut.record(CHAT_ID, "two")
        sut.record(CHAT_ID, "three")

        assertEquals(listOf("one", "two", "three"), sut.recent(CHAT_ID))
    }

    @Test
    fun `drops oldest message when window is exceeded`() {
        val sut = adapter()

        sut.record(CHAT_ID, "one")
        sut.record(CHAT_ID, "two")
        sut.record(CHAT_ID, "three")
        sut.record(CHAT_ID, "four")

        assertEquals(listOf("two", "three", "four"), sut.recent(CHAT_ID))
    }

    @Test
    fun `histories are scoped per chat`() {
        val sut = adapter()

        sut.record(CHAT_ID, "in chat A")
        sut.record(OTHER_CHAT_ID, "in chat B")

        assertEquals(listOf("in chat A"), sut.recent(CHAT_ID))
        assertEquals(listOf("in chat B"), sut.recent(OTHER_CHAT_ID))
    }

    @Test
    fun `evicts conversations not touched since threshold`() {
        val sut = adapter()
        sut.record(CHAT_ID, "stale")
        sut.record(OTHER_CHAT_ID, "fresh")

        // simulate that CHAT_ID's last-seen is well in the past by using a future threshold for the fresh one
        sut.evictStaleBefore(Instant.now().plusSeconds(1))
        // both should have been touched before "now + 1s", so both are evicted
        assertTrue(sut.recent(CHAT_ID).isEmpty())
        assertTrue(sut.recent(OTHER_CHAT_ID).isEmpty())
    }

    @Test
    fun `keeps conversations newer than threshold`() {
        val sut = adapter()
        sut.record(CHAT_ID, "fresh")

        sut.evictStaleBefore(Instant.now().minusSeconds(60))

        assertEquals(listOf("fresh"), sut.recent(CHAT_ID))
    }

    companion object {
        private const val CHAT_ID = 100L
        private const val OTHER_CHAT_ID = 200L
    }
}

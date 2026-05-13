package com.kvsiniuk.parleybot.infrastructure.history

import com.kvsiniuk.parleybot.port.output.ChatHistoryPortOut
import org.springframework.ai.chat.memory.ChatMemory
import org.springframework.ai.chat.messages.UserMessage
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.Duration
import java.time.Instant
import java.util.concurrent.ConcurrentHashMap

@Component
class InMemoryChatHistoryAdapter(
    private val chatMemory: ChatMemory,
) : ChatHistoryPortOut {
    private val lastSeen = ConcurrentHashMap<Long, Instant>()

    override fun recent(chatId: Long): List<String> = chatMemory.get(chatId.toString()).mapNotNull { it.text }

    override fun record(
        chatId: Long,
        message: String,
    ) {
        chatMemory.add(chatId.toString(), UserMessage(message))
        lastSeen[chatId] = Instant.now()
    }

    @Scheduled(fixedRate = EVICTION_INTERVAL_MS)
    fun evictStale() {
        evictStaleBefore(Instant.now().minus(STALE_AFTER))
    }

    internal fun evictStaleBefore(threshold: Instant) {
        lastSeen.entries.removeIf { (chatId, seen) ->
            if (seen.isBefore(threshold)) {
                chatMemory.clear(chatId.toString())
                true
            } else {
                false
            }
        }
    }

    companion object {
        private const val EVICTION_INTERVAL_MS = 60L * 60L * 1000L
        private val STALE_AFTER: Duration = Duration.ofHours(1)
    }
}

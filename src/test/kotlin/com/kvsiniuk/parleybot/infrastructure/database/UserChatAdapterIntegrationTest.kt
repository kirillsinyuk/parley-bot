package com.kvsiniuk.parleybot.infrastructure.database

import com.kvsiniuk.parleybot.application.model.Language
import com.kvsiniuk.parleybot.application.model.UserChat
import com.kvsiniuk.parleybot.config.JpaTestConfiguration
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.TestPropertySource
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(JpaTestConfiguration::class)
@TestPropertySource(
    properties = [
        "spring.datasource.url=jdbc:sqlite::memory:",
        "spring.datasource.hikari.maximum-pool-size=1",
        "spring.jpa.hibernate.ddl-auto=create-drop",
    ],
)
class UserChatAdapterIntegrationTest {

    @Autowired
    private lateinit var userChatRepository: UserChatRepository

    private lateinit var adapter: UserChatAdapter

    @BeforeEach
    fun setup() {
        adapter = UserChatAdapter(userChatRepository)
        userChatRepository.deleteAll()
    }

    @Test
    fun `returns languages from all other users in the same chat`() {
        save(userId = 1L, chatId = 100L, languages = setOf(Language.ES))
        save(userId = 2L, chatId = 100L, languages = setOf(Language.DE))

        val result = adapter.findLanguagesForChat(chatId = 100L, excludeUserId = 99L)

        assertEquals(setOf(Language.ES, Language.DE), result.toSet())
    }

    @Test
    fun `excludes the sender's own language preferences`() {
        save(userId = 1L, chatId = 100L, languages = setOf(Language.ES))
        save(userId = 2L, chatId = 100L, languages = setOf(Language.DE))

        val result = adapter.findLanguagesForChat(chatId = 100L, excludeUserId = 1L)

        assertEquals(listOf(Language.DE), result)
    }

    @Test
    fun `returns each language only once when multiple users want the same language`() {
        save(userId = 1L, chatId = 100L, languages = setOf(Language.ES))
        save(userId = 2L, chatId = 100L, languages = setOf(Language.ES))

        val result = adapter.findLanguagesForChat(chatId = 100L, excludeUserId = 99L)

        assertEquals(1, result.size)
        assertEquals(Language.ES, result.first())
    }

    @Test
    fun `returns empty list when the chat has no members`() {
        val result = adapter.findLanguagesForChat(chatId = 100L, excludeUserId = 1L)

        assertTrue(result.isEmpty())
    }

    @Test
    fun `does not include languages from a different chat`() {
        save(userId = 1L, chatId = 100L, languages = setOf(Language.ES))
        save(userId = 2L, chatId = 200L, languages = setOf(Language.DE))

        val result = adapter.findLanguagesForChat(chatId = 100L, excludeUserId = 99L)

        assertEquals(listOf(Language.ES), result)
    }

    @Test
    fun `returns multiple languages when a user has subscribed to several`() {
        save(userId = 1L, chatId = 100L, languages = setOf(Language.ES, Language.FR))

        val result = adapter.findLanguagesForChat(chatId = 100L, excludeUserId = 99L)

        assertEquals(setOf(Language.ES, Language.FR), result.toSet())
    }

    // ── helpers ────────────────────────────────────────────────────────────

    private fun save(userId: Long, chatId: Long, languages: Set<Language>) =
        userChatRepository.save(UserChat(userId = userId, chatId = chatId, languages = languages))
}

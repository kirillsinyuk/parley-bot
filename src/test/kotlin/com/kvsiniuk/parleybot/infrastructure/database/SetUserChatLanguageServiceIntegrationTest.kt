package com.kvsiniuk.parleybot.infrastructure.database

import com.kvsiniuk.parleybot.application.model.Language
import com.kvsiniuk.parleybot.application.model.UserChat
import com.kvsiniuk.parleybot.application.service.SetUserChatLanguageService
import com.kvsiniuk.parleybot.config.JpaTestConfiguration
import com.kvsiniuk.parleybot.port.input.model.SetLanguagesRequest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.TestPropertySource
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

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
class SetUserChatLanguageServiceIntegrationTest {

    @Autowired
    private lateinit var userChatRepository: UserChatRepository

    private lateinit var service: SetUserChatLanguageService

    @BeforeEach
    fun setup() {
        service = SetUserChatLanguageService(userChatRepository)
        userChatRepository.deleteAll()
    }

    @Test
    fun `creates a new record when the user has no preferences for this chat yet`() {
        service.setLanguages(SetLanguagesRequest(chatId = 100L, userId = 1L, languages = setOf(Language.ES)))

        val saved = userChatRepository.findByUserIdAndChatId(1L, 100L)
        assertNotNull(saved)
        assertEquals(setOf(Language.ES), saved.languages)
    }

    @Test
    fun `updates language preferences for a user who already has a record`() {
        userChatRepository.save(UserChat(userId = 1L, chatId = 100L, languages = setOf(Language.EN)))

        service.setLanguages(SetLanguagesRequest(chatId = 100L, userId = 1L, languages = setOf(Language.ES, Language.DE)))

        val updated = userChatRepository.findByUserIdAndChatId(1L, 100L)
        assertNotNull(updated)
        assertEquals(setOf(Language.ES, Language.DE), updated.languages)
    }

    @Test
    fun `two users in the same chat maintain independent language preferences`() {
        service.setLanguages(SetLanguagesRequest(chatId = 100L, userId = 1L, languages = setOf(Language.ES)))
        service.setLanguages(SetLanguagesRequest(chatId = 100L, userId = 2L, languages = setOf(Language.DE)))

        assertEquals(setOf(Language.ES), userChatRepository.findByUserIdAndChatId(1L, 100L)!!.languages)
        assertEquals(setOf(Language.DE), userChatRepository.findByUserIdAndChatId(2L, 100L)!!.languages)
    }

    @Test
    fun `same user can have different language settings in different chats`() {
        service.setLanguages(SetLanguagesRequest(chatId = 100L, userId = 1L, languages = setOf(Language.ES)))
        service.setLanguages(SetLanguagesRequest(chatId = 200L, userId = 1L, languages = setOf(Language.JA)))

        assertEquals(setOf(Language.ES), userChatRepository.findByUserIdAndChatId(1L, 100L)!!.languages)
        assertEquals(setOf(Language.JA), userChatRepository.findByUserIdAndChatId(1L, 200L)!!.languages)
    }

    @Test
    fun `updating languages does not create a duplicate record`() {
        service.setLanguages(SetLanguagesRequest(chatId = 100L, userId = 1L, languages = setOf(Language.EN)))
        service.setLanguages(SetLanguagesRequest(chatId = 100L, userId = 1L, languages = setOf(Language.ES)))

        assertEquals(1, userChatRepository.findAllByChatId(100L).size)
    }
}

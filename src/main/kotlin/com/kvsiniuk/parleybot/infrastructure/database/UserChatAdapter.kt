package com.kvsiniuk.parleybot.infrastructure.database

import com.kvsiniuk.parleybot.application.model.Language
import com.kvsiniuk.parleybot.port.output.UserChatPortOut
import org.springframework.stereotype.Component

@Component
class UserChatAdapter(
    private val userChatRepository: UserChatRepository,
) : UserChatPortOut {
    override fun findLanguagesForChat(
        chatId: Long,
        excludeUserId: Long,
    ): List<Language> =
        userChatRepository.findAllByChatId(chatId)
            .filter { it.userId != excludeUserId }
            .flatMap { it.languages }
            .distinct()
}

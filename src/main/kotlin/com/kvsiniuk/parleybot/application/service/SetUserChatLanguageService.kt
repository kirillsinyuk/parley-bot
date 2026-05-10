package com.kvsiniuk.parleybot.application.service

import com.kvsiniuk.parleybot.application.model.UserChat
import com.kvsiniuk.parleybot.infrastructure.database.UserChatRepository
import com.kvsiniuk.parleybot.port.input.SetUserChatLanguagePortIn
import com.kvsiniuk.parleybot.port.input.model.SetLanguagesRequest
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class SetUserChatLanguageService(
    private val userChatRepository: UserChatRepository,
) : SetUserChatLanguagePortIn {
    @Transactional
    override fun setLanguages(request: SetLanguagesRequest) {
        (
            userChatRepository.findByUserIdAndChatId(request.userId, request.chatId)
                ?: UserChat(userId = request.userId, chatId = request.chatId)
        ).apply { languages = request.languages }
            .let { u -> userChatRepository.save(u) }
    }
}

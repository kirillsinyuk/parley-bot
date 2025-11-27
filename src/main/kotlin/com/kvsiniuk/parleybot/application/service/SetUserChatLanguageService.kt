package com.kvsiniuk.parleybot.application.service

import com.kvsiniuk.parleybot.application.model.User
import com.kvsiniuk.parleybot.infrastructure.database.UserRepository
import com.kvsiniuk.parleybot.port.input.SetUserChatLanguagePortIn
import com.kvsiniuk.parleybot.port.input.model.SetLanguageRequest
import org.springframework.stereotype.Component

@Component
class SetUserChatLanguageService(
    private val userRepository: UserRepository,
) : SetUserChatLanguagePortIn {
    override fun setLanguage(request: SetLanguageRequest) {
        (
            userRepository.findByUserIdAndChatId(request.userId, request.chatId)
                ?: User(
                    userId = request.userId,
                    chatId = request.chatId,
                )
        )
            .apply { language = request.language }
            .let { u -> userRepository.save(u) }
    }
}

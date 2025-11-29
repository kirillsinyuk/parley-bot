package com.kvsiniuk.parleybot.application.service

import com.kvsiniuk.parleybot.infrastructure.database.UserChatRepository
import com.kvsiniuk.parleybot.port.input.DeleteUserChatPortIn
import com.kvsiniuk.parleybot.port.input.model.DeleteUserChatRequest
import org.springframework.stereotype.Component

@Component
class DeleteUserChatService(
    private val userChatRepository: UserChatRepository,
) : DeleteUserChatPortIn {
    override fun deleteUserChat(request: DeleteUserChatRequest) {
        userChatRepository.deleteByUserId(request.userId)
    }
}

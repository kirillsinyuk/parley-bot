package com.kvsiniuk.parleybot.infrastructure.database

import com.kvsiniuk.parleybot.application.model.UserChat
import org.springframework.data.repository.CrudRepository
import java.util.UUID

interface UserChatRepository : CrudRepository<UserChat, UUID> {
    fun findAllByChatId(chatId: Long): List<UserChat>

    fun findByUserId(userId: Long): UserChat?

    fun findByUserIdAndChatId(
        userId: Long,
        chatId: Long,
    ): UserChat?

    fun deleteByUserId(userId: Long): Boolean
}

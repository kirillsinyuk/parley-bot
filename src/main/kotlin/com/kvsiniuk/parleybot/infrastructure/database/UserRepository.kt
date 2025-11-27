package com.kvsiniuk.parleybot.infrastructure.database

import com.kvsiniuk.parleybot.application.model.User
import org.springframework.data.repository.CrudRepository
import java.util.UUID

interface UserRepository : CrudRepository<User, UUID> {
    fun findAllByChatId(chatId: Long): List<User>

    fun findByUserId(userId: Long): User?

    fun findByUserIdAndChatId(
        userId: Long,
        chatId: Long,
    ): User?

    fun deleteByUserId(userId: Long): Boolean
}

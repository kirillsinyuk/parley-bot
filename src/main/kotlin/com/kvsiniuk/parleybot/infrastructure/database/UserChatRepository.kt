package com.kvsiniuk.parleybot.infrastructure.database

import com.kvsiniuk.parleybot.application.model.UserChat
import org.springframework.data.repository.CrudRepository

interface UserChatRepository : CrudRepository<UserChat, String> {
    fun findAllByChatId(chatId: Long): List<UserChat>

    fun findByUserId(userId: Long): UserChat?

    fun findByUserIdAndChatId(
        userId: Long,
        chatId: Long,
    ): UserChat?

    fun deleteByUserIdAndChatId(userId: Long, chatId: Long): Boolean
}

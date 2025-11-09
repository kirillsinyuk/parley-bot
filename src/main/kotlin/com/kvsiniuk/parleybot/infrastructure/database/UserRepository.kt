package com.kvsiniuk.parleybot.infrastructure.database

import com.kvsiniuk.parleybot.application.model.User
import org.springframework.data.mongodb.repository.MongoRepository

interface UserRepository : MongoRepository<User, Long> {
    fun findByChatId(chatId: Long): User?

    fun findByUserIdAndChatId(userId: Long, chatId: Long): User?
}

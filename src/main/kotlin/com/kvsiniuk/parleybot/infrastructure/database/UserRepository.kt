package com.kvsiniuk.parleybot.infrastructure.database

import com.kvsiniuk.parleybot.application.model.User
import org.springframework.data.mongodb.repository.MongoRepository

interface UserRepository : MongoRepository<User, Long> {
    fun findByBotChatId(chatId: Long): User?

    fun findByUserId(userId: Long): User?
}

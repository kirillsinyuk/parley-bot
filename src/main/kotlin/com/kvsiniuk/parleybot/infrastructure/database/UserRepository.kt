package com.kvsiniuk.parleybot.infrastructure.database

import com.kvsiniuk.parleybot.application.model.User
import java.util.UUID
import org.springframework.data.mongodb.repository.MongoRepository

interface UserRepository : MongoRepository<User, UUID> {
	fun findAllByChatId(chatId: Long): List<User>

	fun findByUserIdAndChatId(userId: Long, chatId: Long): User?
}

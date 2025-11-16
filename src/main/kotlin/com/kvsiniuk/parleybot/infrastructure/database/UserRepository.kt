package com.kvsiniuk.parleybot.infrastructure.database

import com.kvsiniuk.parleybot.application.model.User
import java.util.UUID
import org.springframework.data.repository.CrudRepository

interface UserRepository : CrudRepository<User, UUID> {
	fun findAllByChatId(chatId: Long): List<User>

	fun findByUserIdAndChatId(userId: Long, chatId: Long): User?

	fun deleteByUserId(userId: Long): Boolean
}

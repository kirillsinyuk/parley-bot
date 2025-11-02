package com.kvsiniuk.parleybot.application.service

import com.kvsiniuk.parleybot.application.model.User
import com.kvsiniuk.parleybot.infrastructure.database.UserRepository
import com.kvsiniuk.parleybot.port.`in`.CreateUserPortIn
import org.springframework.stereotype.Component

@Component
class CreateUserService(
	private val userRepository: UserRepository,
) : CreateUserPortIn {
	override fun createNewUser(user: User) =
		userRepository.findByUserId(user.userId)
			?: user
				.apply {
					botChatId = user.botChatId
					userName = user.userName
				}.let { u -> userRepository.save(u) }
}
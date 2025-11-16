package com.kvsiniuk.parleybot.application.service

import com.kvsiniuk.parleybot.infrastructure.database.UserRepository
import com.kvsiniuk.parleybot.port.`in`.DeleteUserPortIn
import com.kvsiniuk.parleybot.port.`in`.model.DeleteUserRequest
import org.springframework.stereotype.Component

@Component
class DeleteUserService(
	private val userRepository: UserRepository,
) : DeleteUserPortIn {

	override fun deleteUser(request: DeleteUserRequest) {
		userRepository.deleteByUserId(request.userId)
	}
}
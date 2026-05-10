package com.kvsiniuk.parleybot.application.service

import com.kvsiniuk.parleybot.application.model.User
import com.kvsiniuk.parleybot.infrastructure.database.UserRepository
import com.kvsiniuk.parleybot.port.input.UserPortIn
import org.springframework.retry.annotation.Retryable
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class UserService(
    private val userRepository: UserRepository,
) : UserPortIn {
    @Retryable
    @Transactional
    override fun incUserMessageCount(userId: Long) {
        getUser(userId)
            .also { it.incMessageCount() }
            .also { userRepository.save(it) }
    }

    @Retryable
    @Transactional
    override fun incUserExplainCount(userId: Long) {
        getUser(userId)
            .also { it.incExplainCount() }
            .also { userRepository.save(it) }
    }

    @Retryable
    @Transactional
    override fun incUserVoiceCount(userId: Long) {
        getUser(userId)
            .also { it.incVoiceCount() }
            .also { userRepository.save(it) }
    }

    override fun getUser(userId: Long): User =
        userRepository.findByUserId(userId)
            ?: User(userId = userId)
}

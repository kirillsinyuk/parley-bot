package com.kvsiniuk.parleybot.application.service

import com.kvsiniuk.parleybot.infrastructure.database.UserRepository
import com.kvsiniuk.parleybot.port.`in`.ExplainMessagePortIn
import com.kvsiniuk.parleybot.port.out.ExplainMessagePortOut
import mu.KLogging
import org.springframework.stereotype.Component

@Component
class ExplainMessageService(
	private val userRepository: UserRepository,
	private val explainMessagePort: ExplainMessagePortOut,
) : ExplainMessagePortIn {

	override fun getExplanation(text: String, userId: Long) =
		userRepository.findByUserId(userId)
			?.let { explainMessagePort.explainMessage(text, it.language.languageName) }
			?: "No user was found. Please, set the language"

	companion object : KLogging()


}
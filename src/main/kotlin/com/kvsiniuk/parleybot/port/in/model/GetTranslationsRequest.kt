package com.kvsiniuk.parleybot.port.`in`.model

data class GetTranslationsRequest(
	val chatId: Long,
	val userId: Long,
	val message: String,
	val replyTo: String?,
)

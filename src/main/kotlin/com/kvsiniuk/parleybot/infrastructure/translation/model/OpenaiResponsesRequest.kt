package com.kvsiniuk.parleybot.infrastructure.translation.model

data class OpenaiResponsesRequest(
	val model: String = "gpt-4.1-nano",
	val input: List<Input>,
)

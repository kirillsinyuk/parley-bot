package com.kvsiniuk.parleybot.infrastructure.translation.model

data class OpenaiResponsesResponse(
	val output: List<OpenaiResponsesOutputData> = listOf(),
)

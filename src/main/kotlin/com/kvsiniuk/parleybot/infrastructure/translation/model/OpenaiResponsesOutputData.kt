package com.kvsiniuk.parleybot.infrastructure.translation.model

data class OpenaiResponsesOutputData(
	val type: String,
	val content: List<OpenaiResponsesContentData>,
)

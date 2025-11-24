package com.kvsiniuk.parleybot.port.`in`

interface ExplainMessagePortIn {
	fun getExplanation(text: String, userId: Long): String
}

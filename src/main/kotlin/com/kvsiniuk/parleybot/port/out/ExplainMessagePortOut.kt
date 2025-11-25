package com.kvsiniuk.parleybot.port.out

interface ExplainMessagePortOut {
	fun explainMessage(text: String, language: String): String
}

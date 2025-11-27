package com.kvsiniuk.parleybot.port.input

interface ExplainMessagePortIn {
    fun getExplanation(
        text: String,
        userId: Long,
    ): String
}

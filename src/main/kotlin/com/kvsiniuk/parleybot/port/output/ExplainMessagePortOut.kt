package com.kvsiniuk.parleybot.port.output

interface ExplainMessagePortOut {
    fun explainMessage(
        text: String,
        language: String,
    ): String
}

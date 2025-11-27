package com.kvsiniuk.parleybot.port.output

interface TranslationPortOut {
    fun translate(
        text: String,
        language: String,
        context: String?,
    ): String?
}

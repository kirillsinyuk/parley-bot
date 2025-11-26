package com.kvsiniuk.parleybot.port.out

interface TranslationPortOut {
    fun translate(text: String, language: String, context: String?): String?
}

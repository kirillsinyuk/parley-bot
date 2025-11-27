package com.kvsiniuk.parleybot.port.output

interface LanguageComparatorPortOut {
    fun haveSameLanguage(
        sourceText: String,
        targetLanguage: String,
    ): Boolean
}

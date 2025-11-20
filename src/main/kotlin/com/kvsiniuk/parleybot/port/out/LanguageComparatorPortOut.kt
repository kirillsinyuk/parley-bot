package com.kvsiniuk.parleybot.port.out

interface LanguageComparatorPortOut {
    fun haveSameLanguage(sourceText: String, translatedText: String): Boolean
}

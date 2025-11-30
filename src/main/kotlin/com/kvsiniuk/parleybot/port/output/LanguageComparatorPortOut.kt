package com.kvsiniuk.parleybot.port.output

interface LanguageComparatorPortOut {
    fun wasTranslated(
        sourceText: String,
        targetText: String,
    ): Boolean
}

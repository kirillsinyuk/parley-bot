package com.kvsiniuk.parleybot.port.output

import com.kvsiniuk.parleybot.port.output.model.TranslationContext

interface TranslationPortOut {
    fun translate(
        text: String,
        language: String,
        context: TranslationContext,
    ): String?
}

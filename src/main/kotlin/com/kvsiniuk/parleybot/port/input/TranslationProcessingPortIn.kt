package com.kvsiniuk.parleybot.port.input

import com.kvsiniuk.parleybot.port.input.model.GetTranslationsRequest

interface TranslationProcessingPortIn {
    fun getTranslations(request: GetTranslationsRequest): List<String>
}

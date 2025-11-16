package com.kvsiniuk.parleybot.port.`in`

import com.kvsiniuk.parleybot.port.`in`.model.GetTranslationsRequest

interface TranslationProcessingPortIn {
	fun getTranslations(request: GetTranslationsRequest): List<String>
}

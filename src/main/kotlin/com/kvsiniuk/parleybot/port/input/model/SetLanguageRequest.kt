package com.kvsiniuk.parleybot.port.input.model

import com.kvsiniuk.parleybot.application.model.Language

data class SetLanguageRequest(
    val chatId: Long,
    val userId: Long,
    val language: Language,
)

package com.kvsiniuk.parleybot.port.input.model

import com.kvsiniuk.parleybot.application.model.Language

data class SetLanguagesRequest(
    val chatId: Long,
    val userId: Long,
    val languages: Set<Language>,
)

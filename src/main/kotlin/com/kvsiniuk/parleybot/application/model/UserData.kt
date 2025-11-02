package com.kvsiniuk.parleybot.application.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class UserData(
    @Id
    val chatId: Long,
    val userName: String,
) {
}

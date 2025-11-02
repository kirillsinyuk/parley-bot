package com.kvsiniuk.parleybot.application.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class GroupChat(
    @Id
    val chatId: Long,
    val targetChatId: Long,
    val creatorUserId: Long
) {
}

package com.kvsiniuk.parleybot.application.model

import java.time.LocalDateTime
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.annotation.Version
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class User(
    @Id
    val userId: Long,
    var botChatId: Long,
    var userName: String,
    @Version
    val version: Long = 0,
    @CreatedDate
    val createdDate: LocalDateTime = LocalDateTime.now(),
    @LastModifiedDate
    val updatedDate: LocalDateTime = LocalDateTime.now(),
)

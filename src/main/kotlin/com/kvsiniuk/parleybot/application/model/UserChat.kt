package com.kvsiniuk.parleybot.application.model

import com.kvsiniuk.parleybot.infrastructure.database.converter.LanguageListConverter
import jakarta.persistence.Column
import jakarta.persistence.Convert
import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.persistence.Version
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "user_chat")
@EntityListeners(AuditingEntityListener::class)
class UserChat(
    @Id
    var id: String = UUID.randomUUID().toString(),
    var userId: Long = 0,
    var chatId: Long = 0,
    @Convert(converter = LanguageListConverter::class)
    @Column(name = "languages", columnDefinition = "TEXT")
    var languages: Set<Language> = setOf(Language.EN),
    @CreatedDate
    var createdDate: LocalDateTime? = null,
    @LastModifiedDate
    var updatedDate: LocalDateTime? = null,
    @Version
    var version: Long? = null,
)

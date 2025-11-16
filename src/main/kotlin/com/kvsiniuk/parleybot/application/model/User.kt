package com.kvsiniuk.parleybot.application.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.persistence.Version
import java.time.LocalDateTime
import java.util.UUID
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate

@Entity
@Table(name = "users")
open class User(

    @Id
    @Column(nullable = false)
    var id: UUID = UUID.randomUUID(),

    var userId: Long = 0,

    var chatId: Long = 0,

    @Enumerated(EnumType.STRING)
    var language: Language = Language.EN,

    @Version
    var version: Long = 0,

    @CreatedDate
    var createdDate: LocalDateTime = LocalDateTime.now(),

    @LastModifiedDate
    var updatedDate: LocalDateTime = LocalDateTime.now()

) {
    // Hibernate requires a no-arg constructor
    protected constructor() : this(
        id = UUID.randomUUID(),
        userId = 0,
        chatId = 0,
        language = Language.EN,
        version = 0,
        createdDate = LocalDateTime.now(),
        updatedDate = LocalDateTime.now()
    )
}

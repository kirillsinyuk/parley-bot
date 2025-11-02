package com.kvsiniuk.parleybot.infrastructure.database

import com.kvsiniuk.parleybot.application.model.UserData
import org.springframework.data.mongodb.repository.MongoRepository

interface UserDataRepository : MongoRepository<UserData, Long> {
    fun findByChatId(chatId: Long): UserData?
}

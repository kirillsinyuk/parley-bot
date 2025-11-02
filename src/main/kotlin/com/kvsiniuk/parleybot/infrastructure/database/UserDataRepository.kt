package com.kvsiniuk.parleybot.infrastructure.database

import com.kvsinyuk.stickergenerator.applicaiton.domain.BotData
import org.springframework.data.mongodb.repository.MongoRepository

interface UserDataRepository : MongoRepository<BotData, Long> {
    fun findByChatId(chatId: Long): BotData?
}

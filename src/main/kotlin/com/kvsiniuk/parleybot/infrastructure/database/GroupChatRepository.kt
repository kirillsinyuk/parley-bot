package com.kvsiniuk.parleybot.infrastructure.database

import com.kvsiniuk.parleybot.application.model.GroupChat
import org.springframework.data.mongodb.repository.MongoRepository

interface GroupChatRepository : MongoRepository<GroupChat, Long>

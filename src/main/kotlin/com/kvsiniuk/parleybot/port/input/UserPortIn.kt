package com.kvsiniuk.parleybot.port.input

import com.kvsiniuk.parleybot.application.model.User

interface UserPortIn {
    fun incUserMessageCount(userId: Long)

    fun incUserExplainCount(userId: Long)

    fun incUserVoiceCount(userId: Long)

    fun getUser(userId: Long): User
}

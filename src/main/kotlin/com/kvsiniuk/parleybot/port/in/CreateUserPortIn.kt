package com.kvsiniuk.parleybot.port.`in`

import com.kvsiniuk.parleybot.application.model.User

interface CreateUserPortIn {
    fun createNewUser(user: User): User
}

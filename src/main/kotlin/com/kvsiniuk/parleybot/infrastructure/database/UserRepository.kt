package com.kvsiniuk.parleybot.infrastructure.database

import com.kvsiniuk.parleybot.application.model.User
import org.springframework.data.repository.CrudRepository

interface UserRepository : CrudRepository<User, String> {
    fun findByUserId(userId: Long): User?

    fun deleteByUserId(userId: Long): Boolean
}

package com.kvsiniuk.parleybot.port.`in`

import com.kvsiniuk.parleybot.port.`in`.model.DeleteUserRequest

interface DeleteUserPortIn {
    fun deleteUser(request: DeleteUserRequest)
}

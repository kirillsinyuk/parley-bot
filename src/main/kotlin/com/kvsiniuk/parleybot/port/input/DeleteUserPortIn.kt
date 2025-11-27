package com.kvsiniuk.parleybot.port.input

import com.kvsiniuk.parleybot.port.input.model.DeleteUserRequest

interface DeleteUserPortIn {
    fun deleteUser(request: DeleteUserRequest)
}

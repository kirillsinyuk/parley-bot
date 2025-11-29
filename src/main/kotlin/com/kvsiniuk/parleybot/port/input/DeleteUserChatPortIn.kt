package com.kvsiniuk.parleybot.port.input

import com.kvsiniuk.parleybot.port.input.model.DeleteUserChatRequest

interface DeleteUserChatPortIn {
    fun deleteUserChat(request: DeleteUserChatRequest)
}

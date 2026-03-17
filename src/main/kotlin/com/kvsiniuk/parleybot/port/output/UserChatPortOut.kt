package com.kvsiniuk.parleybot.port.output

import com.kvsiniuk.parleybot.application.model.Language

interface UserChatPortOut {
    fun findLanguagesForChat(chatId: Long, excludeUserId: Long): List<Language>
}

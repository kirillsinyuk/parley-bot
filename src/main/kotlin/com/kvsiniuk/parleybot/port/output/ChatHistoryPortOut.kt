package com.kvsiniuk.parleybot.port.output

interface ChatHistoryPortOut {
    fun recent(chatId: Long): List<String>

    fun record(
        chatId: Long,
        message: String,
    )
}

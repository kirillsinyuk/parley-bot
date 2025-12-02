package com.kvsiniuk.parleybot.port.output

import com.kvsiniuk.parleybot.application.model.Voice

interface TelegramFilePortOut {
    fun getFileContent(fileId: String): Voice
}

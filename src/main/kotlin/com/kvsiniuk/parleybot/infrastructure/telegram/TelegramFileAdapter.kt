package com.kvsiniuk.parleybot.infrastructure.telegram

import com.kvsiniuk.parleybot.application.model.Voice
import com.kvsiniuk.parleybot.port.output.TelegramFilePortOut
import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.request.GetFile
import org.springframework.stereotype.Component

@Component
class TelegramFileAdapter(
    private val bot: TelegramBot,
) : TelegramFilePortOut {
    override fun getFileContent(fileId: String): Voice {
        val file = bot.execute(GetFile(fileId))
        val fileName = file.file().filePath().substringAfterLast("/")
        return Voice(bot.getFileContent(file.file()), fileName)
    }
}

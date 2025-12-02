package com.kvsiniuk.parleybot.adapter.mapper

import com.kvsiniuk.parleybot.application.model.TelegramUpdateMessage
import com.kvsiniuk.parleybot.config.MapstructConfig
import com.pengrad.telegrambot.model.Update
import org.mapstruct.Mapper

@Mapper(config = MapstructConfig::class)
abstract class TelegramUpdateMessageMapper {
    fun toMessage(update: Update) =
        TelegramUpdateMessage(
            message = update.message()?.text(),
            replyText = update.message()?.replyToMessage()?.text(),
            chatId = update.message()?.chat()?.id() ?: 0,
            userId = update.message()?.from()?.id() ?: update.message()?.leftChatMember()?.id() ?: 0,
            userLeftGroup = update.message()?.leftChatMember()?.id() != null,
            language = update.message()?.from()?.languageCode(),
            voiceFileId = update.message()?.voice()?.fileId(),
        )
}

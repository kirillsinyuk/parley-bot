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
			chatId = update.message()?.chat()?.id() ?: 0,
			userId = update.message()?.from()?.id() ?: 0
		)
}

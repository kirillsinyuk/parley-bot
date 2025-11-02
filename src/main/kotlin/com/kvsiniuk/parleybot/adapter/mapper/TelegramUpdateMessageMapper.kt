package com.kvsiniuk.parleybot.adapter.mapper

import com.kvsiniuk.parleybot.application.model.TelegramUpdateMessage
import com.kvsiniuk.parleybot.config.MapstructConfig
import com.pengrad.telegrambot.model.Update
import org.mapstruct.Mapper
import org.mapstruct.Mapping
@Mapper(config = MapstructConfig::class)
abstract class TelegramUpdateMessageMapper {
	@Mapping(target = "chatId", expression = "java(update.message().chat().id())")
	@Mapping(target = "message", expression = "java(update.message().text())")
	abstract fun toMessage(update: Update): TelegramUpdateMessage
}

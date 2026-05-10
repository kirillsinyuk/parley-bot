package com.kvsiniuk.parleybot.adapter.telegram

import com.kvsiniuk.parleybot.adapter.mapper.TelegramUpdateMessageMapper
import com.kvsiniuk.parleybot.adapter.telegram.handler.TelegramUpdateHandler
import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.UpdatesListener
import com.pengrad.telegrambot.model.Update
import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.annotation.PostConstruct
import org.springframework.stereotype.Component

private val logger = KotlinLogging.logger {}

@Component
class BotUpdatesListener(
    private val bot: TelegramBot,
    private val telegramUpdateHandlers: List<TelegramUpdateHandler>,
    private val telegramUpdateMessageMapper: TelegramUpdateMessageMapper,
) : UpdatesListener {
    @PostConstruct
    fun init() {
        bot.setUpdatesListener(this)
    }

    override fun process(updates: MutableList<Update>?): Int {
        updates?.forEach { update ->
            try {
                val updateData = telegramUpdateMessageMapper.toMessage(update)
                logger.debug { "Processing update $updateData" }
                telegramUpdateHandlers
                    .filter { it.canApply(updateData) }
                    .forEach { it.process(updateData) }
            } catch (e: Exception) {
                logger.error(e) { "Failed to process update: $update" }
            }
        }
        return UpdatesListener.CONFIRMED_UPDATES_ALL
    }
}

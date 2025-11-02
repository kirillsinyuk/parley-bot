package com.kvsiniuk.parleybot.adapter.telegram.handler

import com.kvsiniuk.parleybot.application.model.TelegramUpdateMessage

interface TelegramUpdateHandler {
    fun process(update: TelegramUpdateMessage)

    fun canApply(update: TelegramUpdateMessage): Boolean
}

package com.kvsiniuk.parleybot.application.model

enum class BotCommand(
    val command: String,
    val description: String? = null,
) {
    START("/start"),
    HELP("/help"),
    SET_LANG("/set_lang", "Set language"),
    EXIT("/exit", "Leave translation"),
    FEEDBACK("/feedback"),
}

val MENU_COMMANDS = setOf(BotCommand.SET_LANG, BotCommand.EXIT)

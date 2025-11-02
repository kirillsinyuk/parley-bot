package com.kvsiniuk.parleybot.application.model

enum class BotCommand(
    val command: String,
    val description: String? = null,
) {
    START("/start"),
    HELP("/help"),
    SET_LANG("/set_lang", "Set language"),
    NEW_CHAT("/new_chat", "Create new chat with another user"),
}

val MENU_COMMANDS =
    setOf(
	    BotCommand.SET_LANG,
	    BotCommand.NEW_CHAT,
    )

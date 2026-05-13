package com.kvsiniuk.parleybot.port.output.model

data class TranslationContext(
    val recentMessages: List<String> = emptyList(),
    val replyTo: String? = null,
) {
    fun isEmpty(): Boolean = recentMessages.isEmpty() && replyTo.isNullOrBlank()
}

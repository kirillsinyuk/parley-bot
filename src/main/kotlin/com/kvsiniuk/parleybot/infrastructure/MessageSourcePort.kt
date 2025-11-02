package com.kvsiniuk.parleybot.infrastructure

import org.springframework.context.MessageSource
import org.springframework.stereotype.Component
import java.util.Locale

interface MessageSourcePort {
    fun getMessage(code: String): String
}

@Component
class MessageSourcePortImpl(
    private val messageSource: MessageSource,
) : MessageSourcePort {
    override fun getMessage(code: String): String {
        return messageSource.getMessage(code, null, Locale.ENGLISH)
    }
}

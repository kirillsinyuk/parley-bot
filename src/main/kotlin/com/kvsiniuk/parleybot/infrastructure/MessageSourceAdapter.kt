package com.kvsiniuk.parleybot.infrastructure

import com.kvsiniuk.parleybot.port.out.MessageSourcePortOut
import org.springframework.context.MessageSource
import org.springframework.stereotype.Component
import java.util.Locale

@Component
class MessageSourcePortOutImpl(
    private val messageSource: MessageSource,
) : MessageSourcePortOut {
    override fun getMessage(code: String): String {
        return messageSource.getMessage(code, null, Locale.ENGLISH)
    }
}

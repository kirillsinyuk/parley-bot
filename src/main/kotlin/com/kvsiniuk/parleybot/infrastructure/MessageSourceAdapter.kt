package com.kvsiniuk.parleybot.infrastructure

import com.kvsiniuk.parleybot.port.output.MessageSourcePortOut
import org.springframework.context.MessageSource
import org.springframework.stereotype.Component
import java.util.Locale

@Component
class MessageSourceAdapter(
    private val messageSource: MessageSource,
) : MessageSourcePortOut {
    override fun getMessage(
        code: String,
        locale: String,
    ): String = messageSource.getMessage(code, null, Locale.forLanguageTag(locale))
}

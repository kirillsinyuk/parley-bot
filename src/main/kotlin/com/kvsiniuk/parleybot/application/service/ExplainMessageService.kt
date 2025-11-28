package com.kvsiniuk.parleybot.application.service

import com.kvsiniuk.parleybot.port.input.ExplainMessagePortIn
import com.kvsiniuk.parleybot.port.output.ExplainMessagePortOut
import mu.KLogging
import org.springframework.stereotype.Component

@Component
class ExplainMessageService(
    private val explainMessagePort: ExplainMessagePortOut,
) : ExplainMessagePortIn {
    override fun getExplanation(
        text: String,
        userId: Long,
        userLanguageCode: String,
    ) = explainMessagePort.explainMessage(text, userLanguageCode)

    companion object : KLogging()
}

package com.kvsiniuk.parleybot.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "open-ai")
data class OpenaiConfigurationProperties(
    val host: String,
    val apiKey: String,
)

package com.kvsiniuk.parleybot.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "admin")
data class AdminConfigurationProperties(
    val chatId: Long,
)

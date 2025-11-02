package com.kvsiniuk.parleybot.config

import com.pengrad.telegrambot.TelegramBot
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class BotConfiguration {
    @Value("\${bot.token}")
    private lateinit var token: String

    @Bean
    fun bot() = TelegramBot(token)
}

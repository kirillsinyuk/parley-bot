package com.kvsiniuk.parleybot.config

import org.springframework.ai.chat.memory.ChatMemory
import org.springframework.ai.chat.memory.MessageWindowChatMemory
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.retry.annotation.EnableRetry
import org.springframework.scheduling.annotation.EnableScheduling

@Configuration
@EnableRetry
@EnableScheduling
@EnableJpaAuditing
@EnableConfigurationProperties(value = [AdminConfigurationProperties::class])
class AppConfiguration {
    @Bean
    fun chatMemory(): ChatMemory =
        MessageWindowChatMemory
            .builder()
            .maxMessages(3)
            .build()
}

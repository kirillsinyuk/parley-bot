package com.kvsiniuk.parleybot.config

import com.openai.client.okhttp.OpenAIOkHttpClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class TranslateConfig {
    @Bean
    fun openaiClient(openaiConfigurationProperties: OpenaiConfigurationProperties) =
        OpenAIOkHttpClient.builder()
            .apiKey(openaiConfigurationProperties.apiKey)
            .build()
}

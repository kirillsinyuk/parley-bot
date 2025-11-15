package com.kvsiniuk.parleybot.config

import com.google.cloud.translate.v3.TranslationServiceClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class TranslateConfig {

	@Bean
	fun translationServiceClient(): TranslationServiceClient = TranslationServiceClient.create()
}
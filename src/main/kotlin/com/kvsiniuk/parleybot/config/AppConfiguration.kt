package com.kvsiniuk.parleybot.config

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.retry.annotation.EnableRetry

@Configuration
@EnableRetry
@EnableConfigurationProperties(value = [OpenaiConfigurationProperties::class, AdminConfigurationProperties::class])
class AppConfiguration

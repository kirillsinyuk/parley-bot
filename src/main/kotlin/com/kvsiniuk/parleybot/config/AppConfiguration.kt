package com.kvsiniuk.parleybot.config

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.retry.annotation.EnableRetry

@Configuration
@EnableRetry
@EnableJpaAuditing
@EnableConfigurationProperties(value = [AdminConfigurationProperties::class])
class AppConfiguration

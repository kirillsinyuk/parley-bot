package com.kvsiniuk.parleybot.config

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(value = [GoogleConfigurationProperties::class])
class AppConfiguration

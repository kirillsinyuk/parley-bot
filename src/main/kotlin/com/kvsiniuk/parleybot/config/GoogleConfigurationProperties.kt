package com.kvsiniuk.parleybot.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "google")
data class GoogleConfigurationProperties(
	val projectId: String,
	val location: String,
)
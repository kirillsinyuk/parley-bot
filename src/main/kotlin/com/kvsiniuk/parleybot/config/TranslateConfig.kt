package com.kvsiniuk.parleybot.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.http.client.ClientHttpRequestInterceptor
import org.springframework.web.client.RestClient

@Configuration
class TranslateConfig {

	@Bean
	fun openaiClient(openaiConfigurationProperties: OpenaiConfigurationProperties, openaiAuthInterceptor: ClientHttpRequestInterceptor) =
		RestClient.builder()
			.baseUrl(openaiConfigurationProperties.host)
			.defaultHeader("Content-Type", APPLICATION_JSON_VALUE)
			.requestInterceptor(openaiAuthInterceptor)
			.build()

	@Bean
	fun openaiAuthInterceptor(openaiConfigurationProperties: OpenaiConfigurationProperties): ClientHttpRequestInterceptor =
		ClientHttpRequestInterceptor { request, body, execution ->
			request.headers.add("Authorization", "Bearer ${openaiConfigurationProperties.apiKey}")
			execution.execute(request, body)
		}
}
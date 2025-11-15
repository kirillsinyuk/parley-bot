package com.kvsiniuk.parleybot.application.service

import com.google.cloud.translate.v3.LocationName
import com.google.cloud.translate.v3.TranslateTextRequest
import com.google.cloud.translate.v3.TranslationServiceClient
import com.kvsiniuk.parleybot.application.model.Language
import com.kvsiniuk.parleybot.config.GoogleConfigurationProperties
import org.springframework.stereotype.Component

@Component
class TranslateService(
	private val translationClient: TranslationServiceClient,
	private val googleConfigurationProperties: GoogleConfigurationProperties
) {

	private final val parent = LocationName.of(googleConfigurationProperties.projectId, googleConfigurationProperties.location)
		.toString()

	fun translate(text: String, language: Language): String {
		val request = TranslateTextRequest.newBuilder()
			.setParent(parent)
			.addContents(text)
			.setTargetLanguageCode(language.isoCode)
			.build()

		val response = translationClient.translateText(request)

		return response.translationsList.first().translatedText
	}
}
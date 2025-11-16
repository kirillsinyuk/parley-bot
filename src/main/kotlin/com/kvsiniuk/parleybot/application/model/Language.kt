package com.kvsiniuk.parleybot.application.model

enum class Language(val isoCode: String) {
	RU("ru"),
	EN("en"),
	ES("es"),
	GE("ka"),
	JA("ja"),
	DE("de"),
	IT("it");
}

fun stringToEnum(input: String): Language? {
	return try {
		enumValueOf<Language>(input)
	} catch (e: IllegalArgumentException) {
		null
	}
}
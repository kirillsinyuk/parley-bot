package com.kvsiniuk.parleybot.application.model

enum class Language {
	RU,
	EN,
	ES;
}

fun stringToEnum(input: String): Language? {
	return try {
		enumValueOf<Language>(input)
	} catch (e: IllegalArgumentException) {
		null
	}
}
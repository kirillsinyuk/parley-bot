package com.kvsiniuk.parleybot.infrastructure.database.converter

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.kvsiniuk.parleybot.application.model.Language
import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter

@Converter
class LanguageListConverter : AttributeConverter<Set<Language>, String> {
    override fun convertToDatabaseColumn(attribute: Set<Language>?): String = MAPPER.writeValueAsString(attribute ?: emptySet<Language>())

    override fun convertToEntityAttribute(dbData: String?): Set<Language> =
        if (dbData.isNullOrBlank()) {
            emptySet()
        } else {
            MAPPER.readValue(dbData, object : TypeReference<Set<Language>>() {})
        }

    companion object {
        private val MAPPER = ObjectMapper()
    }
}

package com.kvsiniuk.parleybot.infrastructure.database.converter

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.kvsiniuk.parleybot.application.model.Language
import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter

@Converter
class LanguageListConverter : AttributeConverter<Set<Language>, String> {
    private val mapper = ObjectMapper()

    override fun convertToDatabaseColumn(attribute: Set<Language>?): String = mapper.writeValueAsString(attribute ?: emptySet<Language>())

    override fun convertToEntityAttribute(dbData: String?): Set<Language> =
        if (dbData.isNullOrBlank()) {
            emptySet()
        } else {
            mapper.readValue(dbData, object : TypeReference<Set<Language>>() {})
        }
}

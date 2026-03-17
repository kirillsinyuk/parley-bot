package com.kvsiniuk.parleybot.application.model

enum class Language(val languageName: String) {
    EN("english"),
    ES("spanish"),
    FR("french"),
    GE("georgian"),
    JA("japanese"),
    DE("deutsch"),
    RU("russian"),
    IT("italian"),
}

fun stringToEnum(input: String): Language? = Language.entries.find { it.name == input }

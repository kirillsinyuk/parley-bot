package com.kvsiniuk.parleybot.port.output

interface MessageSourcePortOut {
    fun getMessage(
        code: String,
        locale: String,
    ): String
}

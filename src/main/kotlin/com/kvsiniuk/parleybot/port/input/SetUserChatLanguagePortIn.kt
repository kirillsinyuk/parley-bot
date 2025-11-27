package com.kvsiniuk.parleybot.port.input

import com.kvsiniuk.parleybot.port.input.model.SetLanguageRequest

interface SetUserChatLanguagePortIn {
    fun setLanguage(request: SetLanguageRequest)
}

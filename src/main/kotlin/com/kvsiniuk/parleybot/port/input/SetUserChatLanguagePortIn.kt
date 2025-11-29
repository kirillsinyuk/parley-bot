package com.kvsiniuk.parleybot.port.input

import com.kvsiniuk.parleybot.port.input.model.SetLanguagesRequest

interface SetUserChatLanguagePortIn {
    fun setLanguages(request: SetLanguagesRequest)
}

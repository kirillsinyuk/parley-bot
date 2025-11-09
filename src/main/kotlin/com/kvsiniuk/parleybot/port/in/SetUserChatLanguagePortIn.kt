package com.kvsiniuk.parleybot.port.`in`

import com.kvsiniuk.parleybot.port.`in`.model.SetLanguageRequest

interface SetUserChatLanguagePortIn {
    fun setLanguage(request: SetLanguageRequest)
}

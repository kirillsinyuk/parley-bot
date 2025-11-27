package com.kvsiniuk.parleybot.port.output

import java.io.File

interface TextToSpeechPortOut {
    fun translateToVoice(text: String): File
}

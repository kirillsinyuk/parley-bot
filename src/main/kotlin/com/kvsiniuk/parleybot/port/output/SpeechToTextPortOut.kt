package com.kvsiniuk.parleybot.port.output

interface SpeechToTextPortOut {
    fun translateToText(file: ByteArray): String?
}

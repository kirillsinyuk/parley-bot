package com.kvsiniuk.parleybot.port.out

interface TextToSpeechPortOut {
	fun translateToVoice(text: String): ByteArray
}

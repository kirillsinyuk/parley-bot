package com.kvsiniuk.parleybot.port.out

import java.io.File

interface TextToSpeechPortOut {
	fun translateToVoice(text: String): File
}

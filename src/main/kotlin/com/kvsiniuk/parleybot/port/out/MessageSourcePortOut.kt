package com.kvsiniuk.parleybot.port.out

interface MessageSourcePortOut {
	fun getMessage(code: String): String
}
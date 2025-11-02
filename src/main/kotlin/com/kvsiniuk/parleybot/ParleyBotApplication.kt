package com.kvsiniuk.parleybot

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ParleyBotApplication

fun main(args: Array<String>) {
	runApplication<ParleyBotApplication>(*args)
}

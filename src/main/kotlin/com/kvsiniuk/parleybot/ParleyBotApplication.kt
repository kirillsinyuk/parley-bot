package com.kvsiniuk.parleybot

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties
class ParleyBotApplication

fun main(args: Array<String>) {
	runApplication<ParleyBotApplication>(*args)
}

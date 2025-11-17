package com.kvsiniuk.parleybot.infrastructure.translation.model

data class Input(
	val role: String,
	val content: String,
)

fun withSystemRole(content: String) = Input("system", content)
fun withUserRole(content: String) = Input("user", content)

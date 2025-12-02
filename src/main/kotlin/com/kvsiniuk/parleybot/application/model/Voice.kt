package com.kvsiniuk.parleybot.application.model

data class Voice(
    val file: ByteArray,
    val fileName: String,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Voice

        if (fileName != other.fileName) return false
        if (file.size != other.file.size) return false

        return true
    }

    override fun hashCode(): Int {
        return fileName.hashCode() + file.size
    }
}

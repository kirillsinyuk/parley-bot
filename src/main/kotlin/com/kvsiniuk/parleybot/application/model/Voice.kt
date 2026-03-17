package com.kvsiniuk.parleybot.application.model

data class Voice(
    val file: ByteArray,
    val fileName: String,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Voice
        return fileName == other.fileName && file.contentEquals(other.file)
    }

    override fun hashCode(): Int = 31 * fileName.hashCode() + file.contentHashCode()
}

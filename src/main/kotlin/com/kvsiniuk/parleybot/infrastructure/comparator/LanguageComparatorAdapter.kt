package com.kvsiniuk.parleybot.infrastructure.comparator

import com.kvsiniuk.parleybot.port.output.LanguageComparatorPortOut
import mu.KLogging
import org.springframework.retry.annotation.Backoff
import org.springframework.retry.annotation.Retryable
import org.springframework.stereotype.Component
import java.text.Normalizer
import kotlin.math.sqrt

@Component
class LanguageComparatorAdapter : LanguageComparatorPortOut {
    @Retryable(backoff = Backoff(delay = 100, multiplier = 2.0))
    override fun wasTranslated(
        sourceText: String,
        targetText: String,
    ): Boolean {
        logger.info("Processing text comparison. Source=$sourceText. TargetText=$targetText")
        // Fast script check. 0-127 - ASCII characters
        if (sourceText.any { it.code > 127 } != targetText.any { it.code > 127 }) {
            return true
        }

        val sourceTokens = normalize(sourceText)
        val targetTokens = normalize(targetText)

        // Computes a word intersection rate
        val overlap = tokenOverlap(sourceTokens, targetTokens)

        // Computes a cosine similarity between character–frequency vectors of the source and translated texts
        // 1.0 -> perfect match
        // 0.9–0.7 -> likely same language
        val charSim =
            cosineSim(
                charDistribution(sourceText.lowercase()),
                charDistribution(targetText.lowercase()),
            )

        // Very rough shape metric: average token length
        val shapeA = sourceTokens.map { it.length }.average()
        val shapeB = targetTokens.map { it.length }.average()
        val shapeSim = 1.0 - (kotlin.math.abs(shapeA - shapeB) / 10.0)

        return !(overlap > 0.25 || (charSim > 0.85 && shapeSim > 0.85))
    }

    fun normalize(text: String): List<String> =
        Normalizer.normalize(text.lowercase(), Normalizer.Form.NFD)
            .replace("\\p{M}".toRegex(), "") // remove diacritics
            .replace("[^\\p{L}\\p{Nd} ]+".toRegex(), " ") // keep only letters/digits
            .split(" ")
            .filter { it.length >= 3 }

    fun tokenOverlap(
        a: List<String>,
        b: List<String>,
    ): Double {
        if (a.isEmpty() || b.isEmpty()) return 0.0
        val setA = a.toSet()
        val setB = b.toSet()
        val intersection = setA.intersect(setB).size
        val union = setA.union(setB).size
        return intersection.toDouble() / union
    }

    fun charDistribution(text: String): Map<Char, Int> =
        text.filter { it.isLetter() }
            .groupingBy { it }
            .eachCount()

    fun cosineSim(
        a: Map<Char, Int>,
        b: Map<Char, Int>,
    ): Double {
        val keys = a.keys + b.keys
        var dot = 0.0
        var normA = 0.0
        var normB = 0.0

        for (k in keys) {
            val x = a[k]?.toDouble() ?: 0.0
            val y = b[k]?.toDouble() ?: 0.0
            dot += x * y
            normA += x * x
            normB += y * y
        }
        return if (normA == 0.0 || normB == 0.0) 0.0 else dot / (sqrt(normA) * sqrt(normB))
    }

    companion object : KLogging()
}

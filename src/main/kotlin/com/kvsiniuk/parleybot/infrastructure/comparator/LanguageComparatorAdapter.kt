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
        logger.debug("Processing text comparison. Source=$sourceText. TargetText=$targetText")
        // Fast script check: if one text uses non-ASCII characters and the other doesn't,
        // they are definitely in different scripts → translation occurred.
        if (sourceText.any { it.code > 127 } != targetText.any { it.code > 127 }) {
            return true
        }

        val sourceTokens = normalize(sourceText)
        val targetTokens = normalize(targetText)

        // Jaccard overlap of word tokens. High overlap means the same words appear in both
        // texts — i.e. the text was not meaningfully changed by translation.
        val overlap = tokenOverlap(sourceTokens, targetTokens)

        // Cosine similarity between character-frequency vectors.
        // Values close to 1.0 indicate the same character distribution (same language).
        val charSim =
            cosineSim(
                charDistribution(sourceText.lowercase()),
                charDistribution(targetText.lowercase()),
            )

        // Average token length as a rough proxy for script/language family.
        // Languages like German or Finnish have longer average words than English or Chinese.
        val shapeA = sourceTokens.map { it.length }.average()
        val shapeB = targetTokens.map { it.length }.average()
        val shapeSim = 1.0 - (kotlin.math.abs(shapeA - shapeB) / 10.0)

        // A translation is considered NOT to have occurred when:
        //   - too many of the same tokens appear in both texts (OVERLAP_THRESHOLD), OR
        //   - both character distribution and word-shape are nearly identical
        //     (CHAR_SIM_THRESHOLD and SHAPE_SIM_THRESHOLD).
        return !(overlap > OVERLAP_THRESHOLD || (charSim > CHAR_SIM_THRESHOLD && shapeSim > SHAPE_SIM_THRESHOLD))
    }

    private fun normalize(text: String): List<String> =
        Normalizer.normalize(text.lowercase(), Normalizer.Form.NFD)
            .replace("\\p{M}".toRegex(), "") // remove diacritics
            .replace("[^\\p{L}\\p{Nd} ]+".toRegex(), " ") // keep only letters/digits
            .split(" ")
            .filter { it.length >= 3 }

    private fun tokenOverlap(
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

    private fun charDistribution(text: String): Map<Char, Int> =
        text.filter { it.isLetter() }
            .groupingBy { it }
            .eachCount()

    private fun cosineSim(
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

    companion object : KLogging() {
        /** Jaccard token overlap above this value means too many shared words → not translated. */
        private const val OVERLAP_THRESHOLD = 0.25

        /** Cosine similarity of character distributions above this value suggests same language. */
        private const val CHAR_SIM_THRESHOLD = 0.85

        /** Average token-length similarity above this value suggests same language family. */
        private const val SHAPE_SIM_THRESHOLD = 0.85
    }
}

package com.kvsiniuk.parleybot.infrastructure.comparator

import org.junit.jupiter.api.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class LanguageComparatorAdapterTest {
    private val comparator = LanguageComparatorAdapter()

    // ── Script-boundary detection ──────────────────────────────────────────

    @Test
    fun `different scripts - one ASCII one non-ASCII - is always translated`() {
        assertTrue(comparator.wasTranslated("hello world", "привет мир"))
    }

    @Test
    fun `both texts use non-ASCII - falls through to heuristic checks`() {
        // Both are Russian; the algorithm should decide "not translated"
        assertFalse(comparator.wasTranslated("привет как дела", "привет как дела"))
    }

    @Test
    fun `english to japanese is detected as translated`() {
        assertTrue(comparator.wasTranslated("good morning everyone", "おはようございます"))
    }

    @Test
    fun `english to chinese is detected as translated`() {
        assertTrue(comparator.wasTranslated("the weather is nice today", "今天天气很好"))
    }

    // ── Token-overlap heuristic ────────────────────────────────────────────

    @Test
    fun `identical texts are not translated`() {
        assertFalse(comparator.wasTranslated("the quick brown fox", "the quick brown fox"))
    }

    @Test
    fun `texts with high token overlap are not treated as translated`() {
        // One extra word — overlap is still very high
        assertFalse(comparator.wasTranslated("the quick brown fox", "the quick brown fox jumps"))
    }

    @Test
    fun `english to spanish with clearly different vocabulary is detected as translated`() {
        assertTrue(
            comparator.wasTranslated(
                "the beautiful house is very large and comfortable",
                "la hermosa casa es muy grande y cómoda",
            ),
        )
    }

    @Test
    fun `english to german is detected as translated`() {
        assertTrue(
            comparator.wasTranslated(
                "the weather is very cold today and it is snowing",
                "das Wetter ist heute sehr kalt und es schneit",
            ),
        )
    }

    @Test
    fun `english to french is detected as translated`() {
        assertTrue(
            comparator.wasTranslated(
                "good morning how are you doing today",
                "bonjour comment allez-vous aujourd'hui",
            ),
        )
    }

    // ── Edge cases ─────────────────────────────────────────────────────────

    @Test
    fun `empty source does not throw`() {
        comparator.wasTranslated("", "hello")
    }

    @Test
    fun `empty target does not throw`() {
        comparator.wasTranslated("hello", "")
    }

    @Test
    fun `both texts empty does not throw`() {
        comparator.wasTranslated("", "")
    }

    @Test
    fun `single word identical text is not translated`() {
        assertFalse(comparator.wasTranslated("hello", "hello"))
    }
}

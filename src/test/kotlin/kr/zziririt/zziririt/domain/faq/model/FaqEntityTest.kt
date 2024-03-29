package kr.zziririt.zziririt.domain.faq.model

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test


class FaqEntityTest {

    @Test
    fun `FaqEntity create test`() {
        val question = "Some question"
        val answer = "Some answer"

        val faqEntity = FaqEntity(question, answer)

        assertEquals(faqEntity.question, question)
        assertEquals(faqEntity.answer, answer)
        assertNull(faqEntity.id)
    }
}
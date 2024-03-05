package kr.zziririt.zziririt.api.faq.dto

import kr.zziririt.zziririt.domain.faq.model.FaqEntity


data class FaqResponse(
    val id: Long,
    val question: String,
    val answer: String,
) {
    companion object {
        fun from(faqEntity: FaqEntity): FaqResponse {
            return FaqResponse(
                id = faqEntity.id!!,
                question = faqEntity.question,
                answer = faqEntity.answer,
            )
        }
    }
}
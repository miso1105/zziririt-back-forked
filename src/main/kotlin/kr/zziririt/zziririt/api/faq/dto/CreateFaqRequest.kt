package kr.zziririt.zziririt.api.faq.dto

import kr.zziririt.zziririt.domain.faq.model.FaqEntity


data class CreateFaqRequest(
    var question: String,
    var answer: String,
) {
    fun toFaqEntity() = FaqEntity(
        question = question,
        answer = answer,
    )
}


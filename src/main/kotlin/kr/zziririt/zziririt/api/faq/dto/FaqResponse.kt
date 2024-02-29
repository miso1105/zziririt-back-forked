package kr.zziririt.zziririt.api.faq.dto

data class FaqResponse (
    val id: Long,
    val question: String,
    val answer: String,
)
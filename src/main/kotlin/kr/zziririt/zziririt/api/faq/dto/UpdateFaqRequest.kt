package kr.zziririt.zziririt.api.faq.dto

data class UpdateFaqRequest(
    var question: String,
    var answer: String,
)
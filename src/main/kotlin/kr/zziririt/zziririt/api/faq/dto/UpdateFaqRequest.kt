package kr.zziririt.zziririt.api.faq.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class UpdateFaqRequest(
    @field:NotBlank(message = "제목을 작성해주세요.")
    @field:Size(min = 1, max = 255, message = "글자수는 255자를 초과할 수 없습니다.")
    var question: String,

    @field:NotBlank(message = "내용을 작성해주세요.")
    @field:Size(min = 1, max = 5000, message = "글자수는 5000자를 초과할 수 없습니다.")
    var answer: String,
)
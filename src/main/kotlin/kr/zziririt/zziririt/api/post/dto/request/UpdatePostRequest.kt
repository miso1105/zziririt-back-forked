package kr.zziririt.zziririt.api.post.dto.request

import jakarta.validation.constraints.Size
import org.springframework.validation.annotation.Validated

@Validated
data class UpdatePostRequest(
    @field:Size(min = 1, max = 255, message = "게시글 제목의 글자수는 {min}자 이상 {max} 자 이하여야 합니다.")
    val title: String,
    @field:Size(min = 1, max = 20000, message = "게시글 내용의 글자수는 {min}자 이상 {max} 자 이하여야 합니다.")
    val content: String,
    val categoryId: Long,
    val privateStatus: Boolean
)

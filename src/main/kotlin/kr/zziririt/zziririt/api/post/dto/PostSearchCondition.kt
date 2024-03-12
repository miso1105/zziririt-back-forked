package kr.zziririt.zziririt.api.post.dto

import jakarta.validation.constraints.Pattern
import kr.zziririt.zziririt.api.post.dto.valid.ValidSearchType
import org.springframework.validation.annotation.Validated

@Validated
data class PostSearchCondition(
    @field:ValidSearchType
    val searchType: String?,
    val searchTerm: String?,
    @field:Pattern(regexp = "^[1-9]\\d*|0?$", message = "page값은 0이상부터 숫자만 가능합니다.")
    val page: String = "1",
    @field:Pattern(regexp = "^(1|[1-9]\\d{0,1}|100)?$", message = "size값은 1이상부터 100까지의 숫자만 가능합니다.")
    val size: String = "30",
) {
    fun makePostSearchCacheKey() =
        "POST_SEARCH::ZziriritCache_Type:${searchType}_Term:${searchTerm}_Page:${page}_Size:${size}"
}
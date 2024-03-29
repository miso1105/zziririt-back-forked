package kr.zziririt.zziririt.api.post.dto

import jakarta.validation.constraints.Pattern
import kr.zziririt.zziririt.api.post.dto.valid.ValidSearchType
import org.springframework.validation.annotation.Validated

@Validated
data class PostSearchCondition(
    @field:ValidSearchType
    val searchType: String?,
    val searchTerm: String?,
    @field:Pattern(regexp = "^\\d+\$", message = "page값은 0이상부터 숫자만 가능합니다.")
    val page: String = "0",
    @field:Pattern(regexp = "^(1|[1-9]\\d{0,1}|100)?$", message = "size값은 1이상부터 100까지의 숫자만 가능합니다.")
    val size: String = "30",
    val categoryId: Long? = null,
) {
    fun makePostSearchCacheKey(boardId: Long): String {
        val sb = StringBuilder()
        val baseKey = "POST_SEARCH::ZziriritCache"
        sb.append(baseKey)
        sb.append("_boardId:${boardId}")
        if (searchType != null) {
            sb.append("_Type:${searchType}")
        }
        if (searchTerm != null) {
            sb.append("_Term:${searchTerm}")
        }
        sb.append("_Page:${page}")
        sb.append("_Size:${size}")
        if (categoryId != null) {
            sb.append("_categoryId:${categoryId}")
        }
        return sb.toString()
    }
}
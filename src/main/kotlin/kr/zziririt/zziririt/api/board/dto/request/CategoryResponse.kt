package kr.zziririt.zziririt.api.board.dto.request

import kr.zziririt.zziririt.domain.board.model.CategoryEntity

data class CategoryResponse(
    val categoryName: String,
    val categoryId: Long,
) {
    companion object{
        fun from(category: CategoryEntity) : CategoryResponse = CategoryResponse (
            categoryName = category.categoryName,
            categoryId = category.id
        )
    }
}

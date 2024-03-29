package kr.zziririt.zziririt.api.board.dto.request

import kr.zziririt.zziririt.domain.board.model.BoardCategoryEntity
import kr.zziririt.zziririt.domain.board.model.CategoryEntity

data class BoardCategoryResponse(
    val boardId: Long,
    val categoryId: Long,
    val categoryName: String,
) {
    companion object {
        fun from(bc: BoardCategoryEntity): BoardCategoryResponse =
            BoardCategoryResponse(
                boardId = bc.board.id!!,
                categoryId = bc.category.id,
                categoryName = bc.category.categoryName
            )
    }
}

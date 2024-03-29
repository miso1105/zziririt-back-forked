package kr.zziririt.zziririt.api.board.dto.response

import kr.zziririt.zziririt.domain.board.model.BoardActStatus
import kr.zziririt.zziririt.domain.board.model.BoardEntity
import kr.zziririt.zziririt.domain.board.model.BoardType

data class BoardUrlSearchResponse(
    val boardId: Long,
    val boardName: String,
    val boardUrl: String,
    val bordType: BoardType,
    val boardActStatus: BoardActStatus,
    val categoryList: List<CategoryResponse>
) {
    companion object {
        fun from(boardEntity: BoardEntity): BoardUrlSearchResponse =
            BoardUrlSearchResponse(
                boardId = boardEntity.id!!,
                boardName = boardEntity.boardName,
                boardUrl = boardEntity.boardUrl,
                bordType = boardEntity.boardType,
                boardActStatus = boardEntity.boardActStatus,
                categoryList = boardEntity.categories.map { CategoryResponse.from(it) }
            )
    }
}

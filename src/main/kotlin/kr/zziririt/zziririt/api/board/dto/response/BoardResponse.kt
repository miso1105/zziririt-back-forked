package kr.zziririt.zziririt.api.board.dto.response

import kr.zziririt.zziririt.domain.board.model.BoardActStatus
import kr.zziririt.zziririt.domain.board.model.BoardEntity
import kr.zziririt.zziririt.domain.board.model.BoardType

data class BoardResponse(
    val boardId: Long,
    val boardName: String,
    val boardUrl: String,
    val memberId: Long,
    val boardType: BoardType,
    val boardActStatus: BoardActStatus,
) {
    companion object {
        fun from(boardEntity: BoardEntity): BoardResponse = BoardResponse(
            boardId = boardEntity.id!!,
            boardName = boardEntity.boardName,
            boardUrl = boardEntity.boardUrl,
            memberId = boardEntity.socialMember.id!!,
            boardType = boardEntity.boardType,
            boardActStatus = boardEntity.boardActStatus
        )
    }
}

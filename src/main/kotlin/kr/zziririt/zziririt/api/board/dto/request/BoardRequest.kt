package kr.zziririt.zziririt.api.board.dto.request

import jakarta.validation.constraints.NotBlank
import kr.zziririt.zziririt.domain.board.model.BoardEntity
import kr.zziririt.zziririt.domain.board.model.BoardType
import kr.zziririt.zziririt.domain.member.model.SocialMemberEntity
import org.springframework.validation.annotation.Validated

@Validated
data class BoardRequest (
    @field:NotBlank
    val boardName: String,
    @field:NotBlank
    val boardUrl: String,
) {
    fun to(socialMemberEntity: SocialMemberEntity, parent: BoardEntity?) = BoardEntity(
        boardName = boardName,
        socialMember = socialMemberEntity,
        parent = parent,
        boardUrl = boardUrl.lowercase(),
        boardType = BoardType.ZZIRIRIT_BOARD
    )
}
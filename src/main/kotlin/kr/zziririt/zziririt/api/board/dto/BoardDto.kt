package kr.zziririt.zziririt.api.board.dto

import jakarta.validation.constraints.NotBlank
import kr.zziririt.zziririt.domain.board.model.BoardEntity
import kr.zziririt.zziririt.domain.member.model.SocialMemberEntity
import org.springframework.validation.annotation.Validated

@Validated
data class BoardDto (
    @field:NotBlank
    val boardName: String,
) {
    fun to(socialMemberEntity: SocialMemberEntity, parent: BoardEntity?) = BoardEntity(
        boardName = boardName,
        socialMember = socialMemberEntity,
        parent = parent,
    )
}
package kr.zziririt.zziririt.api.board.dto

import jakarta.validation.constraints.NotBlank
import kr.zziririt.zziririt.domain.board.model.BoardEntity
import kr.zziririt.zziririt.domain.board.model.FavoriteBoardEntity
import kr.zziririt.zziririt.domain.member.model.SocialMemberEntity
import org.springframework.validation.annotation.Validated

@Validated
data class FavoriteBoardDto(
    @field:NotBlank
    val favoriteBoardId: String
) {
    fun to(socialMemberEntity: SocialMemberEntity, boardEntity: BoardEntity) = FavoriteBoardEntity(
        socialMember = socialMemberEntity,
        board = boardEntity
    )
}

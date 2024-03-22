package kr.zziririt.zziririt.api.board.dto.request

import jakarta.validation.constraints.NotBlank
import kr.zziririt.zziririt.domain.board.model.BoardEntity
import kr.zziririt.zziririt.domain.board.model.BoardType
import kr.zziririt.zziririt.domain.member.model.SocialMemberEntity
import org.springframework.validation.annotation.Validated

@Validated
data class StreamerBoardRequest(
    @field:NotBlank
    val boardName: String,
    @field:NotBlank
    val boardUrl: String,
) {
    fun to(socialMemberEntity: SocialMemberEntity) = BoardEntity(
        boardName = boardName,
        socialMember = socialMemberEntity,
        boardUrl = boardUrl.lowercase(),
        boardType = BoardType.STREAMER_BOARD
    )
}

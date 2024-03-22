package kr.zziririt.zziririt.api.board.dto.request

import jakarta.validation.constraints.NotBlank
import kr.zziririt.zziririt.domain.board.model.StreamerBoardApplicationEntity
import kr.zziririt.zziririt.domain.member.model.SocialMemberEntity
import org.springframework.validation.annotation.Validated

@Validated
data class StreamerBoardApplicationRequest (
    @field:NotBlank
    val applyUrl: String,
    @field:NotBlank
    val applyBoardName: String,
) {
    fun to(socialMemberEntity: SocialMemberEntity) = StreamerBoardApplicationEntity(
        applyUrl = applyUrl,
        applyBoardName = applyBoardName,
        socialMemberEntity = socialMemberEntity
    )
}

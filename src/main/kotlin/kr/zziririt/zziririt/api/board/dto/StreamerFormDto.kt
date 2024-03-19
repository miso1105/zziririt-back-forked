package kr.zziririt.zziririt.api.board.dto

import jakarta.validation.constraints.NotBlank
import kr.zziririt.zziririt.domain.board.model.StreamerFormEntity
import kr.zziririt.zziririt.domain.member.model.SocialMemberEntity
import org.springframework.validation.annotation.Validated

@Validated
data class StreamerFormDto (
    @field:NotBlank
    val applyUrl: String,
    @field:NotBlank
    val applyBoardName: String,
) {
    fun to(socialMemberEntity: SocialMemberEntity) = StreamerFormEntity(
        applyUrl = "https://zziririt.kr/${applyUrl}",
        applyBoardName = applyBoardName,
        socialMemberEntity = socialMemberEntity
    )
}

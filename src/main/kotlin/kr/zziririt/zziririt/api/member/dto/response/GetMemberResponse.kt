package kr.zziririt.zziririt.api.member.dto.response

import kr.zziririt.zziririt.domain.member.model.LoginHistoryEntity
import kr.zziririt.zziririt.domain.member.model.MemberStatus
import kr.zziririt.zziririt.domain.member.model.SocialMemberEntity
import java.time.LocalDateTime

data class GetMemberResponse(
    val nickname: String,
    val memberStatus: MemberStatus,
    val lastLogin: LocalDateTime,
) {
    companion object {
        fun from(
            socialMemberEntity: SocialMemberEntity,
            loginHistoryEntity: LoginHistoryEntity

        ): GetMemberResponse {
            return GetMemberResponse(
                nickname = socialMemberEntity.nickname,
                memberStatus = socialMemberEntity.memberStatus,
                lastLogin = loginHistoryEntity.createdAt
            )
        }
    }
}
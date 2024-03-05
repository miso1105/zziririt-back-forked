package kr.zziririt.zziririt.api.member.dto.response

import kr.zziririt.zziririt.domain.member.model.MemberStatus
import kr.zziririt.zziririt.domain.member.model.SocialMemberEntity

data class GetMemberResponse(
    val nickname: String,
    val memberStatus: MemberStatus
){
    companion object{
        fun from(socialMemberEntity: SocialMemberEntity): GetMemberResponse {
            return GetMemberResponse(
                nickname = socialMemberEntity.nickname,
                memberStatus = socialMemberEntity.memberStatus
            )
        }
    }
}
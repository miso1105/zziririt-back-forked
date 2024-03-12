package kr.zziririt.zziririt.domain.member.repository

import kr.zziririt.zziririt.domain.member.model.SocialMemberEntity


interface SocialMemberRepository {

    fun findByEmail(email: String): SocialMemberEntity?

    fun findByIdOrNull(id: Long): SocialMemberEntity?

    fun findSleeperCandidatesMemberId() : List<Long>

    fun save(entity : SocialMemberEntity) : SocialMemberEntity

    fun bulkUpdateMemberStatusToSleeper(memberIdList: List<Long>)
}
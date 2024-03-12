package kr.zziririt.zziririt.domain.member.repository

import kr.zziririt.zziririt.domain.member.model.LoginHistoryEntity
import kr.zziririt.zziririt.domain.member.model.SocialMemberEntity

interface LoginHistoryRepository {

    fun findTopByMemberIdOrderByCreatedAtDesc(memberId: SocialMemberEntity): LoginHistoryEntity

    fun save(entity: LoginHistoryEntity): LoginHistoryEntity

}
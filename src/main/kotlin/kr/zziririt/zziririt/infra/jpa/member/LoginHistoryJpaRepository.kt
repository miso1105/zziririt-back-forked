package kr.zziririt.zziririt.infra.jpa.member

import kr.zziririt.zziririt.domain.member.model.LoginHistoryEntity
import kr.zziririt.zziririt.domain.member.model.SocialMemberEntity
import org.springframework.data.jpa.repository.JpaRepository

interface LoginHistoryJpaRepository : JpaRepository<LoginHistoryEntity, Long> {

    fun findTopByMemberIdOrderByCreatedAtDesc(memberId: SocialMemberEntity): LoginHistoryEntity

}
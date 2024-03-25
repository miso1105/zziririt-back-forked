package kr.zziririt.zziririt.infra.jpa.member

import kr.zziririt.zziririt.domain.member.model.MemberIconEntity
import org.springframework.data.jpa.repository.JpaRepository

interface MemberIconJpaRepository : JpaRepository<MemberIconEntity, Long> {

    fun existsByMemberIdAndIconId(memberId: Long, iconId: Long): Boolean
}
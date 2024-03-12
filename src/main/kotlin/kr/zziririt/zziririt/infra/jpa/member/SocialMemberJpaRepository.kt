package kr.zziririt.zziririt.infra.jpa.member

import kr.zziririt.zziririt.domain.member.model.SocialMemberEntity
import org.springframework.data.jpa.repository.JpaRepository

interface SocialMemberJpaRepository : JpaRepository<SocialMemberEntity, Long> {

    fun findByEmail(email: String) : SocialMemberEntity?

}
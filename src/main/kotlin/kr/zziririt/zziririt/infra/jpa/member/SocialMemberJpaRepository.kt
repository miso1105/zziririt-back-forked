package kr.zziririt.zziririt.infra.jpa.member

import kr.zziririt.zziririt.domain.member.model.OAuth2Provider
import kr.zziririt.zziririt.domain.member.model.SocialMemberEntity
import org.springframework.data.jpa.repository.JpaRepository

interface SocialMemberJpaRepository : JpaRepository<SocialMemberEntity, Long> {
    fun findByEmail(email: String): SocialMemberEntity?

    fun existsByProviderAndProviderId(provider: OAuth2Provider, providerId: String): Boolean

    fun findByProviderAndProviderId(provider: OAuth2Provider, providerId: String): SocialMemberEntity
}
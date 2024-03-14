package kr.zziririt.zziririt.domain.member.repository

import kr.zziririt.zziririt.domain.member.model.OAuth2Provider
import kr.zziririt.zziririt.domain.member.model.SocialMemberEntity
import kr.zziririt.zziririt.infra.jpa.member.SocialMemberJpaRepository
import kr.zziririt.zziririt.infra.querydsl.member.SocialMemberQueryDslRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

@Repository
class SocialMemberRepositoryImpl(
    private val memberJpaRepository: SocialMemberJpaRepository,
    private val memberQueryDslRepository: SocialMemberQueryDslRepository
) : SocialMemberRepository {
    override fun findByEmail(email: String): SocialMemberEntity? = memberJpaRepository.findByEmail(email)

    override fun findByIdOrNull(id: Long): SocialMemberEntity? = memberJpaRepository.findByIdOrNull(id)

    override fun findSleeperCandidatesMemberId(): List<Long> = memberQueryDslRepository.findSleeperCandidatesMemberId()

    override fun save(entity: SocialMemberEntity): SocialMemberEntity = memberJpaRepository.save(entity)

    override fun bulkUpdateMemberStatusToSleeper(memberIdList: List<Long>) =
        memberQueryDslRepository.bulkUpdateMemberStatusToSleeper(memberIdList)

    override fun existsByProviderAndProviderId(provider: OAuth2Provider, providerId: String): Boolean =
        memberJpaRepository.existsByProviderAndProviderId(provider, providerId)

    override fun findByProviderAndProviderId(provider: OAuth2Provider, providerId: String): SocialMemberEntity =
        memberJpaRepository.findByProviderAndProviderId(provider, providerId)
}
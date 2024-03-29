package kr.zziririt.zziririt.domain.member.repository

import kr.zziririt.zziririt.domain.member.model.MemberIconEntity
import kr.zziririt.zziririt.infra.jpa.member.MemberIconJpaRepository
import kr.zziririt.zziririt.infra.querydsl.member.MemberIconQueryDslRepository
import kr.zziririt.zziririt.infra.querydsl.member.dto.GetMyIconsDto
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository


@Repository
class MemberIconRepositoryImpl(
    private val memberIconJpaRepository: MemberIconJpaRepository,
    private val memberIconQueryDslRepository: MemberIconQueryDslRepository
) : MemberIconRepository {
    override fun save(entity: MemberIconEntity): MemberIconEntity = memberIconJpaRepository.save(entity)

    override fun delete(entity: MemberIconEntity) = memberIconJpaRepository.delete(entity)

    override fun findByIdOrNull(id: Long): MemberIconEntity? = memberIconJpaRepository.findByIdOrNull(id)
    override fun getMyIcons(pageable: Pageable): PageImpl<GetMyIconsDto> =
        memberIconQueryDslRepository.getMyIcons(pageable)

    override fun existsByMemberIdAndIconId(memberId: Long, iconId: Long): Boolean =
        memberIconJpaRepository.existsByMemberIdAndIconId(memberId, iconId)

    override fun findByMemberIdAndIconId(memberId: Long, iconId: Long): MemberIconEntity? =
        memberIconJpaRepository.findByMemberIdAndIconId(memberId, iconId)
}
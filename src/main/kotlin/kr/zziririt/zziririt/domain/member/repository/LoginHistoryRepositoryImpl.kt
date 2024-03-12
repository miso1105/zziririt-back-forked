package kr.zziririt.zziririt.domain.member.repository

import kr.zziririt.zziririt.domain.member.model.LoginHistoryEntity
import kr.zziririt.zziririt.domain.member.model.SocialMemberEntity
import kr.zziririt.zziririt.infra.jpa.member.LoginHistoryJpaRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

@Repository
class LoginHistoryRepositoryImpl(
    private val loginHistoryJpaRepository: LoginHistoryJpaRepository
) : LoginHistoryRepository {

    fun findByIdOrNull(id: Long): LoginHistoryEntity? = loginHistoryJpaRepository.findByIdOrNull(id)

    override fun save(entity: LoginHistoryEntity) = loginHistoryJpaRepository.save(entity)

    override fun findTopByMemberIdOrderByCreatedAtDesc(memberId: SocialMemberEntity): LoginHistoryEntity =
        loginHistoryJpaRepository.findTopByMemberIdOrderByCreatedAtDesc(memberId)


}
package kr.zziririt.zziririt.domain.report.repository

import kr.zziririt.zziririt.domain.member.model.MemberStatus
import kr.zziririt.zziririt.domain.report.model.BannedHistoryEntity
import kr.zziririt.zziririt.infra.jpa.report.BannedHistoryJpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class BannedHistoryRepositoryImpl(
    private val bannedHistoryJpaRepository : BannedHistoryJpaRepository
) : BannedHistoryRepository {

    override fun save(entity: BannedHistoryEntity) = bannedHistoryJpaRepository.save(entity)
    override fun findByBannedEndDateBeforeAndBannedMemberIdMemberStatus(
        currentDate: LocalDateTime,
        status: MemberStatus
    ): List<BannedHistoryEntity> = bannedHistoryJpaRepository.findByBannedEndDateBeforeAndBannedMemberIdMemberStatus(currentDate, status)
}
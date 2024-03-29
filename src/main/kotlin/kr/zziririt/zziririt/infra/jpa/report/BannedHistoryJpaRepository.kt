package kr.zziririt.zziririt.infra.jpa.report

import kr.zziririt.zziririt.domain.member.model.MemberStatus
import kr.zziririt.zziririt.domain.report.model.BannedHistoryEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDateTime

interface BannedHistoryJpaRepository : JpaRepository<BannedHistoryEntity, Long> {
    fun findByBannedEndDateBeforeAndBannedMemberIdMemberStatus(currentDate: LocalDateTime, status: MemberStatus): List<BannedHistoryEntity>
}
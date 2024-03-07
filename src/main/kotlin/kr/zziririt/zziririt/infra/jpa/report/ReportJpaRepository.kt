package kr.zziririt.zziririt.infra.jpa.report

import kr.zziririt.zziririt.domain.report.model.ReportEntity
import org.springframework.data.jpa.repository.JpaRepository

interface ReportJpaRepository : JpaRepository<ReportEntity, Long> {

    fun findAllByReportedMemberId(reportedMemberId: Long): List<ReportEntity>
}
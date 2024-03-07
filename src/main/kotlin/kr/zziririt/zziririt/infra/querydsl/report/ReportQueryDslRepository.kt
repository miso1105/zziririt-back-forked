package kr.zziririt.zziririt.infra.querydsl.report

import kr.zziririt.zziririt.infra.querydsl.report.dto.ReportDto
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface ReportQueryDslRepository {

    fun findByPageable(pageable: Pageable): Page<ReportDto>
    fun searchByReportedMemberId(memberId: Long, pageable: Pageable): Page<ReportDto>

}
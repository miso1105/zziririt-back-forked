package kr.zziririt.zziririt.domain.report.repository

import kr.zziririt.zziririt.domain.report.model.ReportEntity
import kr.zziririt.zziririt.infra.querydsl.report.dto.ReportDto
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface ReportRepository {
    fun save(entity: ReportEntity): ReportEntity

    fun findAll(): List<ReportEntity>

    fun findAllByReportedMemberId(reportedMemberId: Long): List<ReportEntity>

    fun findByPageable(pageable: Pageable): Page<ReportDto>

    fun searchByReportedMemberId(memberId: Long, pageable: Pageable): Page<ReportDto>
}
package kr.zziririt.zziririt.domain.report.repository

import kr.zziririt.zziririt.domain.report.model.ReportEntity
import kr.zziririt.zziririt.infra.jpa.report.ReportJpaRepository
import kr.zziririt.zziririt.infra.querydsl.report.ReportQueryDslRepositoryImpl
import kr.zziririt.zziririt.infra.querydsl.report.dto.ReportDto
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository

@Repository
class ReportRepositoryImpl(
    private val reportJpaRepository: ReportJpaRepository,
    private val reportQueryDslRepositoryImpl: ReportQueryDslRepositoryImpl,
) : ReportRepository {
    override fun save(entity: ReportEntity): ReportEntity = reportJpaRepository.save(entity)

    override fun findAll(): List<ReportEntity> = reportJpaRepository.findAll()
    override fun findAllByReportedMemberId(reportedMemberId: Long): List<ReportEntity> =
        reportJpaRepository.findAllByReportedMemberId(reportedMemberId)

    override fun findByPageable(pageable: Pageable): Page<ReportDto> =
        reportQueryDslRepositoryImpl.findByPageable(pageable)

    override fun searchByReportedMemberId(memberId: Long, pageable: Pageable): Page<ReportDto> =
        reportQueryDslRepositoryImpl.searchByReportedMemberId(memberId, pageable)

}
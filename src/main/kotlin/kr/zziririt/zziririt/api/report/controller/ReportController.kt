package kr.zziririt.zziririt.api.report.controller

import jakarta.validation.Valid
import kr.zziririt.zziririt.api.report.dto.request.BanMemberRequest
import kr.zziririt.zziririt.api.report.dto.request.MemberReportRequest
import kr.zziririt.zziririt.api.report.service.ReportService
import kr.zziririt.zziririt.global.responseEntity
import kr.zziririt.zziririt.infra.security.UserPrincipal
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/cs/report")
class ReportController(
    private val reportService: ReportService
) {

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping()
    fun getReports(
        @PageableDefault(
            size = 30,
            sort = ["created_at"]
        ) pageable: Pageable
    ) = responseEntity(HttpStatus.OK) { reportService.getReportListByPageable(pageable) }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{memberId}")
    fun getPaginatedReportsByMemberId(
        @PathVariable memberId: Long,
        @PageableDefault(
            size = 30,
            sort = ["created_at"]
        ) pageable: Pageable
    ) = responseEntity(HttpStatus.OK) { reportService.getPaginatedReportListByMemberId(memberId, pageable) }

    @PostMapping()
    fun reportMember(
        @Valid @RequestBody request: MemberReportRequest,
        @AuthenticationPrincipal userPrincipal: UserPrincipal
    ) = responseEntity(HttpStatus.OK) { reportService.reportMember(request, userPrincipal) }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/ban")
    fun banMember(
        @Valid @RequestBody request: BanMemberRequest,
    ) = responseEntity(HttpStatus.OK) { reportService.banMember(request) }

}
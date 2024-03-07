package kr.zziririt.zziririt.api.report.service

import jakarta.transaction.Transactional
import kr.zziririt.zziririt.api.report.dto.request.BanMemberRequest
import kr.zziririt.zziririt.api.report.dto.request.MemberReportRequest
import kr.zziririt.zziririt.domain.comment.repository.CommentRepository
import kr.zziririt.zziririt.domain.member.repository.SocialMemberRepository
import kr.zziririt.zziririt.domain.post.repository.PostRepository
import kr.zziririt.zziririt.domain.report.repository.BannedHistoryRepository
import kr.zziririt.zziririt.domain.report.repository.ReportRepository
import kr.zziririt.zziririt.global.exception.ErrorCode
import kr.zziririt.zziririt.global.exception.ModelNotFoundException
import kr.zziririt.zziririt.infra.querydsl.report.dto.ReportDto
import kr.zziririt.zziririt.infra.security.UserPrincipal
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class ReportService(
    private val reportRepository: ReportRepository,
    private val memberRepository: SocialMemberRepository,
    private val postRepository: PostRepository,
    private val commentRepository: CommentRepository,
    private val bannedHistoryRepository: BannedHistoryRepository
) {

    fun getReportListByPageable(pageable: Pageable): Page<ReportDto> {
        return reportRepository.findByPageable(pageable)
    }

    fun getPaginatedReportListByMemberId(memberId: Long, pageable: Pageable): Page<ReportDto> {
        return reportRepository.searchByReportedMemberId(memberId, pageable)
    }

    fun reportMember(request: MemberReportRequest, userPrincipal: UserPrincipal) {
        val reporterMember = memberRepository.findByIdOrNull(userPrincipal.memberId) ?: throw ModelNotFoundException(
            ErrorCode.MODEL_NOT_FOUND
        )
        val reportedMember = memberRepository.findByIdOrNull(request.reportedMemberId)
            ?: throw ModelNotFoundException(ErrorCode.MODEL_NOT_FOUND)
        val reportedPost = request.reportedPostId?.let { postRepository.findByIdOrNull(it) }
        val reportedComment = request.reportedCommentId?.let { commentRepository.findByIdOrNull(it) }

        val saveReport = request.toEntity(reporterMember, reportedMember, reportedPost, reportedComment)

        reportRepository.save(saveReport)
    }

    @Transactional
    fun banMember(request: BanMemberRequest) {
        val banMember =
            memberRepository.findByIdOrNull(request.memberId) ?: throw ModelNotFoundException(ErrorCode.MODEL_NOT_FOUND)
        val reports = reportRepository.findAllByReportedMemberId(banMember.id!!)
        val bannedHistory = request.toEntity(banMember)

        reports.forEach {
            bannedHistory.reportedIdList.add(it.id)
            it.reportProcessed()
        }

        banMember.toBanned()

        bannedHistoryRepository.save(bannedHistory)
    }

}
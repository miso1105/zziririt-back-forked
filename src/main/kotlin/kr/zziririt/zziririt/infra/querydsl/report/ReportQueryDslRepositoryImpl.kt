package kr.zziririt.zziririt.infra.querydsl.report

import kr.zziririt.zziririt.domain.comment.model.QCommentEntity
import kr.zziririt.zziririt.domain.member.model.QSocialMemberEntity
import kr.zziririt.zziririt.domain.member.repository.SocialMemberRepository
import kr.zziririt.zziririt.domain.post.model.QPostEntity
import kr.zziririt.zziririt.domain.report.model.QReportEntity
import kr.zziririt.zziririt.global.exception.ErrorCode
import kr.zziririt.zziririt.global.exception.ModelNotFoundException
import kr.zziririt.zziririt.infra.querydsl.QueryDslSupport
import kr.zziririt.zziririt.infra.querydsl.report.dto.QReportDto
import kr.zziririt.zziririt.infra.querydsl.report.dto.ReportDto
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

@Repository
class ReportQueryDslRepositoryImpl(
    private val memberRepository: SocialMemberRepository
) : QueryDslSupport(), ReportQueryDslRepository {
    private val report = QReportEntity.reportEntity
    private val member = QSocialMemberEntity.socialMemberEntity
    private val post = QPostEntity.postEntity
    private val comment = QCommentEntity.commentEntity
    override fun findByPageable(pageable: Pageable): Page<ReportDto> {
        val result = queryFactory
            .select(
                QReportDto(
                    report.id,
                    report.reporterMember.id,
                    report.reportedMember.id,
                    report.reportedPostId.id,
                    report.reportedCommentId.id,
                    report.reportedReason
                )
            )
            .from(report)
            .leftJoin(report.reportedMember, member)
            .leftJoin(report.reportedPostId, post)
            .leftJoin(report.reportedCommentId, comment)
            .orderBy(report.createdAt.desc())
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .fetch()

        val count = queryFactory
            .select(report.count())
            .from(report)
            .leftJoin(report.reportedMember, member)
            .leftJoin(report.reportedPostId, post)
            .leftJoin(report.reportedCommentId, comment)
            .fetchOne() ?: 0L

        return PageImpl(result, pageable, count)
    }

    override fun searchByReportedMemberId(memberId: Long, pageable: Pageable): Page<ReportDto> {
        val searchedMember =
            memberRepository.findByIdOrNull(memberId) ?: throw ModelNotFoundException(ErrorCode.MODEL_NOT_FOUND)

        val result = queryFactory
            .select(
                QReportDto(
                    report.id,
                    report.reporterMember.id,
                    report.reportedMember.id,
                    report.reportedPostId.id,
                    report.reportedCommentId.id,
                    report.reportedReason
                )
            )
            .from(report)
            .leftJoin(report.reportedMember, member)
            .leftJoin(report.reportedPostId, post)
            .leftJoin(report.reportedCommentId, comment)
            .where(report.reportedMember.eq(searchedMember))
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .fetch()

        val count = queryFactory
            .select(report.count())
            .from(report)
            .leftJoin(report.reportedMember, member)
            .leftJoin(report.reportedPostId, post)
            .leftJoin(report.reportedCommentId, comment)
            .where(report.reportedMember.eq(searchedMember))
            .fetchOne() ?: 0L

        return PageImpl(result, pageable, count)
    }

}
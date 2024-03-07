package kr.zziririt.zziririt.api.report.dto.request

import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import kr.zziririt.zziririt.domain.comment.model.CommentEntity
import kr.zziririt.zziririt.domain.member.model.SocialMemberEntity
import kr.zziririt.zziririt.domain.post.model.PostEntity
import kr.zziririt.zziririt.domain.report.model.ReportEntity

data class MemberReportRequest(
    @field: NotNull
    val reportedMemberId: Long,
    val reportedPostId: Long?,
    val reportedCommentId: Long?,
    @field: Size(min = 1, max = 300, message = "신고 사유는 {min}자 이상, {max}자 이하여야 합니다.")
    val reportedReason: String
) {

    fun toEntity(
        reporterMember: SocialMemberEntity,
        reportedMember: SocialMemberEntity,
        post: PostEntity?,
        comment: CommentEntity?,
    ) = ReportEntity (
        reporterMember = reporterMember,
        reportedMember = reportedMember,
        reportedPostId = post,
        reportedCommentId = comment,
        reportedReason = reportedReason
    )

}




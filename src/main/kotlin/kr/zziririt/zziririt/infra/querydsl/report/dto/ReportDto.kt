package kr.zziririt.zziririt.infra.querydsl.report.dto

import com.querydsl.core.annotations.QueryProjection

data class ReportDto @QueryProjection constructor(
    val id: Long,
    val reporterMemberId: Long,
    val reportedMemberId: Long,
    val reportedPostId: Long?,
    val reportedCommentId: Long?,
    val reportedReason: String,
)
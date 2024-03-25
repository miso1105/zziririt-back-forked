package kr.zziririt.zziririt.infra.querydsl.member.dto

import com.querydsl.core.annotations.QueryProjection
import java.io.Serializable
import java.time.LocalDateTime

data class GetMyIconsDto @QueryProjection constructor(
    val memberId: Long,
    val iconId: Long,
    val createdAt: LocalDateTime,
) : Serializable
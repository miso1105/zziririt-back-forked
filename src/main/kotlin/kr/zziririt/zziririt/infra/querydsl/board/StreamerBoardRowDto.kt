package kr.zziririt.zziririt.infra.querydsl.board

import com.querydsl.core.annotations.QueryProjection

data class StreamerBoardRowDto @QueryProjection constructor(
    val childBoardId: Long,
    val boardName: String,
)

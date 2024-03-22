package kr.zziririt.zziririt.infra.querydsl.board

import com.querydsl.core.annotations.QueryProjection

data class ChildBoardRowDto @QueryProjection constructor(
    val boardId: Long,
    val boardName: String
)

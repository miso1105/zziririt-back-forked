package kr.zziririt.zziririt.infra.querydsl.zzirit.dto

import com.querydsl.core.annotations.QueryProjection

data class ZziritDto @QueryProjection constructor(
    val postId: Long,
    val zziritCount: Long,
    val postTitle: String,
    val boardUrl: String,
    val boardId: Long,
)

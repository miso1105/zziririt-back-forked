package kr.zziririt.zziririt.api.zzirit.dto.response

data class ZziritCountResponse(
    val postId: Long,
    val zziritCount: Long,
    val postTitle: String,
    val boardUrl: String,
    val boardId: Long,
)

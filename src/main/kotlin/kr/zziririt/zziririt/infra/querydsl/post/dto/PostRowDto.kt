package kr.zziririt.zziririt.infra.querydsl.post.dto

import com.querydsl.core.annotations.QueryProjection
import java.io.Serializable
import java.time.LocalDateTime

data class PostRowDto @QueryProjection constructor(
    val postId: Long,
    val zzirit: Long,
    val boardName: String,
    val title: String,
    val memberId: Long,
    val nickname: String,
    val privateStatus: Boolean,
    val hit: Long,
    val createdAt: LocalDateTime
): Serializable {
    var permissionToRead: Boolean = !privateStatus
}

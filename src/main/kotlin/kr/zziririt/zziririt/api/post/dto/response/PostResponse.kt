package kr.zziririt.zziririt.api.post.dto.response

import kr.zziririt.zziririt.domain.post.model.PostEntity
import java.time.LocalDateTime


data class PostResponse(
    val postId: Long,
    val title: String,
    val content: String,
    val memberId: Long,
    val nickname: String,
    val privateStatus: Boolean,
    val blindStatus: Boolean,
    val zziritCount: Long,
    val hit: Long,
    val createdAt: LocalDateTime,
) {
    companion object {
        fun from(postEntity: PostEntity): PostResponse = PostResponse(
            postId = postEntity.id!!,
            title = postEntity.title,
            content = postEntity.content,
            memberId = postEntity.socialMember.id!!,
            nickname = postEntity.socialMember.nickname,
            privateStatus = postEntity.privateStatus,
            blindStatus = postEntity.blindStatus,
            zziritCount = postEntity.zziritCount,
            hit = postEntity.hit,
            createdAt = postEntity.createdAt
        )
    }
}
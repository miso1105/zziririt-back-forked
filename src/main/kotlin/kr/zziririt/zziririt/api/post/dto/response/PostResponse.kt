package kr.zziririt.zziririt.api.post.dto.response

import kr.zziririt.zziririt.api.comment.dto.CommentResponse
import kr.zziririt.zziririt.domain.post.model.PostEntity
import java.time.LocalDateTime

data class PostResponse(
    val postId: Long,
    val title: String,
    val content: String,
    val memberId: Long,
    val nickname: String,
    val boardId: Long,
    val boardName: String,
    val categoryId: Long,
    val categoryName: String,
    val privateStatus: Boolean,
    val blindStatus: Boolean,
    val permissionToUpdateStatus: Boolean,
    val permissionToDeleteStatus: Boolean,
    val zziritCount: Long,
    val hit: Long,
    val commentResponses: List<CommentResponse>?,
    val createdAt: LocalDateTime,
) {
    companion object {
        fun of(
            postEntity: PostEntity,
            permissionToUpdateStatus: Boolean,
            permissionToDeleteStatus: Boolean,
            comments: List<CommentResponse>?
        ): PostResponse = PostResponse(
            postId = postEntity.id!!,
            title = postEntity.title,
            content = postEntity.content,
            memberId = postEntity.socialMember.id!!,
            nickname = postEntity.socialMember.nickname,
            boardId = postEntity.board.id!!,
            boardName = postEntity.board.boardName,
            categoryId = postEntity.category.id,
            categoryName = postEntity.category.categoryName,
            privateStatus = postEntity.privateStatus,
            blindStatus = postEntity.blindStatus,
            permissionToUpdateStatus = permissionToUpdateStatus,
            permissionToDeleteStatus = permissionToDeleteStatus,
            zziritCount = postEntity.zziritCount,
            hit = postEntity.hit,
            commentResponses = comments,
            createdAt = postEntity.createdAt
        )
    }
}
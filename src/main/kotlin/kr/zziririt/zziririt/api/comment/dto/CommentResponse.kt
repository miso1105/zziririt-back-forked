package kr.zziririt.zziririt.api.comment.dto

import kr.zziririt.zziririt.domain.comment.model.CommentEntity

data class CommentResponse (
    val memberId: Long,
    val memberNickname: String,
    val content: String,
    val privateStatus: Boolean,
    val zziritCount: Long,
    val permissionToUpdateStatus: Boolean,
    val permissionToDeleteStatus: Boolean
) {
    companion object {
        fun from(commentEntity: CommentEntity, permissionToUpdateStatus: Boolean, permissionToDeleteStatus: Boolean) = CommentResponse(
            memberId = commentEntity.socialMember.id!!,
            memberNickname = commentEntity.socialMember.nickname,
            content = commentEntity.content,
            privateStatus = commentEntity.privateStatus,
            zziritCount = commentEntity.zziritCount,
            permissionToUpdateStatus = permissionToUpdateStatus,
            permissionToDeleteStatus = permissionToDeleteStatus
        )
    }
}
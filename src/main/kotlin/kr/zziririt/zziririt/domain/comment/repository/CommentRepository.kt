package kr.zziririt.zziririt.domain.comment.repository

import kr.zziririt.zziririt.domain.comment.model.CommentEntity

interface CommentRepository {
    fun save(commentEntity: CommentEntity): CommentEntity
    fun delete(commentEntity: CommentEntity)
    fun findByIdOrNull(commentId: Long): CommentEntity?
    fun findAllByPostId(postId: Long): List<CommentEntity>?
}
package kr.zziririt.zziririt.domain.comment.repository

import kr.zziririt.zziririt.domain.comment.model.CommentEntity
import kr.zziririt.zziririt.infra.jpa.comment.CommentJpaRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

@Repository
class CommentRepositoryImpl(
    private val commentJpaRepository: CommentJpaRepository
):CommentRepository {
    override fun save(commentEntity: CommentEntity): CommentEntity = commentJpaRepository.save(commentEntity)
    override fun delete(commentEntity: CommentEntity) = commentJpaRepository.delete(commentEntity)
    override fun findByIdOrNull(commentId: Long): CommentEntity? = commentJpaRepository.findByIdOrNull(commentId)
    override fun findAllByPostId(postId: Long): List<CommentEntity>? =
        commentJpaRepository.findAllByPostId(postId)
}
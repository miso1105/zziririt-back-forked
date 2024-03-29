package kr.zziririt.zziririt.infra.jpa.comment

import kr.zziririt.zziririt.domain.comment.model.CommentEntity
import org.springframework.data.jpa.repository.JpaRepository

interface CommentJpaRepository : JpaRepository<CommentEntity, Long> {
    fun findAllByPostIdAndPrivateStatus(postId: Long, privateStatus: Boolean): List<CommentEntity>?
}

package kr.zziririt.zziririt.domain.comment.repository

import kr.zziririt.zziririt.domain.comment.model.CommentEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface CommentRepository : CrudRepository<CommentEntity, Long> {
}
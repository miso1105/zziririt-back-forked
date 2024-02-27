package kr.zziririt.zziririt.domain.post.repository

import kr.zziririt.zziririt.domain.post.model.CommentEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface CommentRepository : CrudRepository<CommentEntity, Long> {
}
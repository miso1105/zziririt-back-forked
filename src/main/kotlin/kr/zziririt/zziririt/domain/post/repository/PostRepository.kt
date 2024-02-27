package kr.zziririt.zziririt.domain.post.repository

import kr.zziririt.zziririt.domain.post.model.PostEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface PostRepository : CrudRepository<PostEntity, Long> {
}
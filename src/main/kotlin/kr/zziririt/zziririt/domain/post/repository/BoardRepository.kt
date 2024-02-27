package kr.zziririt.zziririt.domain.post.repository

import kr.zziririt.zziririt.domain.post.model.BoardEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface BoardRepository : CrudRepository<BoardEntity, Long> {
}
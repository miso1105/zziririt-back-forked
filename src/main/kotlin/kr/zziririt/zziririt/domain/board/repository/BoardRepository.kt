package kr.zziririt.zziririt.domain.board.repository

import kr.zziririt.zziririt.domain.board.model.BoardEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface BoardRepository : CrudRepository<BoardEntity, Long> {
}
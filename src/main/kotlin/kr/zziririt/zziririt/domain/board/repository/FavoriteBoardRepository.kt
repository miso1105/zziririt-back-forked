package kr.zziririt.zziririt.domain.board.repository

import kr.zziririt.zziririt.domain.board.model.FavoriteBoardEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface FavoriteBoardRepository : CrudRepository<FavoriteBoardEntity, Long> {
}
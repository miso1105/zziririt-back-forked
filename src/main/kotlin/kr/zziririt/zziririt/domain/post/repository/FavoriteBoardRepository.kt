package kr.zziririt.zziririt.domain.post.repository

import kr.zziririt.zziririt.domain.post.model.FavoriteBoardEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface FavoriteBoardRepository : CrudRepository<FavoriteBoardEntity, Long> {
}
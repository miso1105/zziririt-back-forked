package kr.zziririt.zziririt.domain.point.repository

import kr.zziririt.zziririt.domain.point.model.PointHistoryEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface PointHistoryRepository : CrudRepository<PointHistoryEntity, Long> {
}
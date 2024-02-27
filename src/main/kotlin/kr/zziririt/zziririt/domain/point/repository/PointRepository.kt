package kr.zziririt.zziririt.domain.point.repository

import kr.zziririt.zziririt.domain.point.model.PointEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface PointRepository : CrudRepository<PointEntity, Long> {
}
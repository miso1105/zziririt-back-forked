package kr.zziririt.zziririt.domain.report.repository

import kr.zziririt.zziririt.domain.report.model.BannedHistoryEntity
import org.springframework.data.repository.CrudRepository

interface BannedHistoryRepository : CrudRepository <BannedHistoryEntity, Long> {
}
package kr.zziririt.zziririt.domain.board.repository

import kr.zziririt.zziririt.domain.board.model.StreamerFormEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface StreamerFormRepository: CrudRepository<StreamerFormEntity, Long> {
}
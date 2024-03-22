package kr.zziririt.zziririt.domain.board.repository

import kr.zziririt.zziririt.domain.board.model.StreamerBoardApplicationEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface StreamerBoardApplicationRepository: CrudRepository<StreamerBoardApplicationEntity, Long> {
}
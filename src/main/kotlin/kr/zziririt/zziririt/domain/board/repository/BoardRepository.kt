package kr.zziririt.zziririt.domain.board.repository

import kr.zziririt.zziririt.domain.board.model.BoardEntity
import kr.zziririt.zziririt.infra.querydsl.board.BoardRowDto
import kr.zziririt.zziririt.infra.querydsl.board.StreamerBoardRowDto
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable


interface BoardRepository {
    fun findByIdOrNull(id: Long): BoardEntity?

    fun save(entity: BoardEntity): BoardEntity

    fun delete(entity: BoardEntity)

    fun findAll(): List<BoardEntity>

    fun findAllById(idList: List<Long>): List<BoardEntity>

    fun findByPageable(pageable: Pageable): Page<BoardRowDto>

    fun existsBoardEntityByBoardName(boardName: String): Boolean

    fun findStreamersByPageable(pageable: Pageable): Page<StreamerBoardRowDto>

    fun findInactiveBoardStatus(): List<Long>

    fun updateBoardStatusToInactive(inactiveBoardIdList: List<Long>)

    fun findActiveStatusBoards(pageable: Pageable): Page<BoardRowDto>
}
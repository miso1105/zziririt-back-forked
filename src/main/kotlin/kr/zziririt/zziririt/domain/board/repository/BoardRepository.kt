package kr.zziririt.zziririt.domain.board.repository

import kr.zziririt.zziririt.domain.board.model.BoardEntity
import kr.zziririt.zziririt.infra.querydsl.board.BoardRowDto
import kr.zziririt.zziririt.infra.querydsl.board.StreamerBoardRowDto


interface BoardRepository {
    fun findByIdOrNull(id: Long): BoardEntity?

    fun save(entity: BoardEntity): BoardEntity

    fun delete(entity: BoardEntity)

    fun findAll(): List<BoardEntity>

    fun findAllById(idList: List<Long>): List<BoardEntity>

    fun findBoards(): List<BoardRowDto>

    fun existsBoardEntityByBoardName(boardName: String): Boolean

    fun findStreamers(): List<StreamerBoardRowDto>

    fun findInactiveBoardStatus(): List<Long>

    fun updateBoardStatusToInactive(inactiveBoardIdList: List<Long>)

    fun findActiveStatusBoards(): List<BoardRowDto>

    fun findByBoardUrl(boardUrl: String): BoardEntity

    fun existsBoardEntityByBoardUrl(boardUrl: String): Boolean
}
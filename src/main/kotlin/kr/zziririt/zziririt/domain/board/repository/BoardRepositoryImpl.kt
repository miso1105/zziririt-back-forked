package kr.zziririt.zziririt.domain.board.repository

import kr.zziririt.zziririt.domain.board.model.BoardEntity
import kr.zziririt.zziririt.infra.jpa.board.BoardJpaRepository
import kr.zziririt.zziririt.infra.querydsl.board.BoardQueryDslRepositoryImpl
import kr.zziririt.zziririt.infra.querydsl.board.BoardRowDto
import kr.zziririt.zziririt.infra.querydsl.board.StreamerBoardRowDto
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository


@Repository
class BoardRepositoryImpl(
    private val boardJpaRepository: BoardJpaRepository,
    private val boardQueryDslRepositoryImpl: BoardQueryDslRepositoryImpl
): BoardRepository {
    override fun findByIdOrNull(id: Long): BoardEntity? = boardJpaRepository.findByIdOrNull(id)

    override fun save(entity: BoardEntity): BoardEntity = boardJpaRepository.save(entity)

    override fun delete(entity: BoardEntity) = boardJpaRepository.delete(entity)

    override fun findAll(): List<BoardEntity> = boardJpaRepository.findAll()

    override fun findAllById(idList: List<Long>): List<BoardEntity> = boardJpaRepository.findAllById(idList)

    override fun findBoards(): List<BoardRowDto> = boardQueryDslRepositoryImpl.findBoards()

    override fun existsBoardEntityByBoardName(boardName: String): Boolean = boardJpaRepository.existsBoardEntityByBoardName(boardName)

    override fun findStreamers(): List<StreamerBoardRowDto> = boardQueryDslRepositoryImpl.findStreamers()

    override fun findInactiveBoardStatus(): List<Long> = boardQueryDslRepositoryImpl.findBoardStatusToInactive()

    override fun updateBoardStatusToInactive(inactiveBoardIdList: List<Long>) = boardQueryDslRepositoryImpl.updateBoardStatusToInactive(inactiveBoardIdList)

    override fun findActiveStatusBoards(): List<BoardRowDto> = boardQueryDslRepositoryImpl.findActiveStatusBoards()

    override fun findByBoardUrl(boardUrl: String): BoardEntity = boardJpaRepository.findByBoardUrl(boardUrl)
}

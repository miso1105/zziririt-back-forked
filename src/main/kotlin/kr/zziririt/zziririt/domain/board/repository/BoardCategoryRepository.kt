package kr.zziririt.zziririt.domain.board.repository

import kr.zziririt.zziririt.domain.board.model.BoardCategoryEntity
import org.springframework.data.jpa.repository.JpaRepository

interface BoardCategoryRepository : JpaRepository<BoardCategoryEntity, Long>{

    fun findByBoardId(boardId: Long) : List<BoardCategoryEntity>
}
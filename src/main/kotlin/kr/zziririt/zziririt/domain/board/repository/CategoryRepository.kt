package kr.zziririt.zziririt.domain.board.repository

import kr.zziririt.zziririt.domain.board.model.CategoryEntity
import org.springframework.data.jpa.repository.JpaRepository

interface CategoryRepository : JpaRepository<CategoryEntity, Long> {

    fun findByBoardId(boardId: Long) : List<CategoryEntity>
}
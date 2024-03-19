package kr.zziririt.zziririt.infra.querydsl.board

import kr.zziririt.zziririt.domain.board.model.BoardActStatus
import kr.zziririt.zziririt.domain.board.model.QBoardEntity
import kr.zziririt.zziririt.domain.post.model.QPostEntity
import kr.zziririt.zziririt.infra.querydsl.QueryDslSupport
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class BoardQueryDslRepositoryImpl : QueryDslSupport(), BoardQueryDslRepository {
    private val board = QBoardEntity.boardEntity
    private val post = QPostEntity.postEntity

    override fun findByPageable(pageable: Pageable): Page<BoardRowDto> {
        val content = queryFactory
            .select(
                QBoardRowDto(
                    board.parent.id,
                    board.id,
                    board.boardName
                )
            )
            .from(board)
            .orderBy(board.parent.id.asc())
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .fetch()

        val count = queryFactory
            .select(board.count())
            .from(board)
            .fetchOne() ?: 0L

        return PageImpl(content, pageable, count)
    }

    override fun findStreamersByPageable(pageable: Pageable): Page<StreamerBoardRowDto> {
        val content = queryFactory
            .select(
                QStreamerBoardRowDto(
                    board.id,
                    board.boardName
                )
            )
            .from(board)
            .where(board.parent.isNotNull)
            .orderBy(board.parent.id.asc())
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .fetch()

        val count = queryFactory
            .select(board.count())
            .from(board)
            .fetchOne() ?: 0L

        return PageImpl(content, pageable, count)
    }

    override fun findBoardStatusToInactive(): List<Long> {
        val checkInactiveDate = LocalDateTime.now().minusDays(8)

        return queryFactory.select(board.id).distinct()
            .from(post)
            .leftJoin(board)
            .on(board.id.eq(post.board.id))
            .where(post.modifiedAt.loe(checkInactiveDate))
            .fetch()
    }

    override fun updateBoardStatusToInactive(inactiveBoardIdList: List<Long>) {
        queryFactory
            .update(board)
            .set(board.boardActStatus, BoardActStatus.INACTIVE)
            .where(board.id.`in`(inactiveBoardIdList))
            .execute()
    }

    override fun findActiveStatusBoards(pageable: Pageable): Page<BoardRowDto> {
        val content = queryFactory
            .select(
                QBoardRowDto(
                    board.parent.id,
                    board.id,
                    board.boardName
                )
            ).from(board)
            .where(board.boardActStatus.eq(BoardActStatus.ACTIVE))
            .orderBy(board.parent.id.asc())
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .fetch()

        val count = queryFactory.select(board.count())
            .from(board)
            .fetchOne() ?: 0L

        return PageImpl(content, pageable, count)
    }
}
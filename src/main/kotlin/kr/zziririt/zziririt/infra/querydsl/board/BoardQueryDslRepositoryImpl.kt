package kr.zziririt.zziririt.infra.querydsl.board

import kr.zziririt.zziririt.domain.board.model.BoardActStatus
import kr.zziririt.zziririt.domain.board.model.BoardType
import kr.zziririt.zziririt.domain.board.model.QBoardEntity
import kr.zziririt.zziririt.domain.board.model.QCategoryEntity
import kr.zziririt.zziririt.domain.post.model.QPostEntity
import kr.zziririt.zziririt.infra.querydsl.QueryDslSupport
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class BoardQueryDslRepositoryImpl : QueryDslSupport(), BoardQueryDslRepository {
    private val board = QBoardEntity.boardEntity
    private val post = QPostEntity.postEntity
    private val category = QCategoryEntity.categoryEntity

    override fun findBoards(): List<BoardRowDto> {
        return queryFactory
            .select(
                QBoardRowDto(
                    board.id,
                    board.boardName
                )
            )
            .from(board)
            .orderBy(board.id.asc())
            .fetch()
    }


    override fun findStreamers(): List<StreamerBoardRowDto> {
        return queryFactory
            .select(
                QStreamerBoardRowDto(
                    board.id,
                    board.boardUrl,
                    board.boardName,
                )
            )
            .from(board)
            .where(board.boardType.eq(BoardType.STREAMER_BOARD))
            .orderBy(board.id.asc())
            .fetch()
    }

    override fun findBoardStatusToInactive(): List<Long> {
        val checkInactiveDate = LocalDateTime.now().minusDays(8)

        return queryFactory.select(board.id).distinct()
            .from(post)
            .leftJoin(board)
            .on(board.id.eq(post.board.id))
            .where(post.modifiedAt.loe(checkInactiveDate), board.boardType.eq(BoardType.STREAMER_BOARD))
            .fetch()
    }

    override fun updateBoardStatusToInactive(inactiveBoardIdList: List<Long>) {
        queryFactory
            .update(board)
            .set(board.boardActStatus, BoardActStatus.INACTIVE)
            .where(board.id.`in`(inactiveBoardIdList))
            .execute()
    }

    override fun findActiveStatusBoards(): List<BoardRowDto> {
        return queryFactory
            .select(
                QBoardRowDto(
                    board.id,
                    board.boardName
                )
            ).from(board)
            .where(board.boardActStatus.eq(BoardActStatus.ACTIVE))
            .orderBy(board.id.asc())
            .fetch()
    }
}

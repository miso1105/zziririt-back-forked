package kr.zziririt.zziririt.infra.querydsl.zzirit

import kr.zziririt.zziririt.domain.board.model.QBoardEntity
import kr.zziririt.zziririt.domain.post.model.QPostEntity
import kr.zziririt.zziririt.domain.zzirit.model.QZziritEntity
import kr.zziririt.zziririt.domain.zzirit.model.ZziritEntityType
import kr.zziririt.zziririt.infra.querydsl.QueryDslSupport
import kr.zziririt.zziririt.infra.querydsl.zzirit.dto.QZziritDto
import kr.zziririt.zziririt.infra.querydsl.zzirit.dto.ZziritDto
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class ZziritQueryDslRepositoryImpl(

) : QueryDslSupport(), ZziritQueryDslRepository {
    override fun findZziritRankInPosts(range: LocalDateTime): List<ZziritDto> {
        val zzirit = QZziritEntity.zziritEntity
        val post = QPostEntity.postEntity
        val board = QBoardEntity.boardEntity
        val limitCount = 5L

        val result = queryFactory
            .select(QZziritDto(
                zzirit.entityId,
                post.zziritCount,
                post.title,
                post.board.boardUrl,
                post.board.id
            ))
            .from(zzirit)
            .leftJoin(post)
            .on(zzirit.entityId.eq(post.id)).fetchJoin()
            .leftJoin(board)
            .on(post.board.eq(board)).fetchJoin()
            .where(zzirit.zziritEntityType.eq(ZziritEntityType.POST))
            .where(zzirit.isDeleted.eq(false))
            .where(zzirit.createdAt.after(range))
            .orderBy(post.zziritCount.desc())
            .limit(limitCount)
            .fetch()

        return result
    }
}
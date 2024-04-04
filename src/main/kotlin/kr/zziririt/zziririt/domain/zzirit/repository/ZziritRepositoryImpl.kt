package kr.zziririt.zziririt.domain.zzirit.repository

import kr.zziririt.zziririt.api.zzirit.dto.response.ZziritCountResponse
import kr.zziririt.zziririt.domain.zzirit.model.ZziritEntity
import kr.zziririt.zziririt.domain.zzirit.model.ZziritEntityType
import kr.zziririt.zziririt.infra.jpa.zzirit.ZziritJpaRepository
import kr.zziririt.zziririt.infra.querydsl.zzirit.ZziritQueryDslRepository
import kr.zziririt.zziririt.infra.querydsl.zzirit.dto.ZziritDto
import kr.zziririt.zziririt.infra.redis.zzirit.ZziritRedisRepository
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class ZziritRepositoryImpl(
    private val zziritJpaRepository: ZziritJpaRepository,
    private val zziritQueryDslRepository: ZziritQueryDslRepository,
    private val zziritRedisRepository: ZziritRedisRepository
) : ZziritRepository {
    override fun save(entity: ZziritEntity): ZziritEntity = zziritJpaRepository.save(entity)
    override fun findZziritByMemberIdAndPostIdOrNull(
        socialMemberId: Long,
        postId: Long
    ): ZziritEntity? =
        zziritJpaRepository.findBySocialMemberIdAndEntityIdAndZziritEntityType(socialMemberId, postId, ZziritEntityType.POST)

    override fun findZziritByMemberIdAndCommentIdOrNull(
        socialMemberId: Long,
        commentId: Long
    ): ZziritEntity? =
        zziritJpaRepository.findBySocialMemberIdAndEntityIdAndZziritEntityType(socialMemberId, commentId, ZziritEntityType.COMMENT)

    override fun countZziritByPostId(postId: Long): Long =
        zziritJpaRepository.countByIsZziritAndEntityIdAndZziritEntityTypeAndIsDeleted(
            true,
            postId,
            ZziritEntityType.POST,
            false
        )

    override fun countZziritByCommentId(commentId: Long): Long =
        zziritJpaRepository.countByIsZziritAndEntityIdAndZziritEntityTypeAndIsDeleted(
            true,
            commentId,
            ZziritEntityType.COMMENT,
            false
        )

    override fun findZziritRankInPosts(range: LocalDateTime): List<ZziritDto> =
        zziritQueryDslRepository.findZziritRankInPosts(range)

    override fun updateRank(zziritRank: List<ZziritDto>) =
        zziritRedisRepository.updateRank(zziritRank)

    override fun findZziritPostRankingInRedis(): List<ZziritCountResponse>? =
        zziritRedisRepository.findZziritPostRankingInRedis()
}
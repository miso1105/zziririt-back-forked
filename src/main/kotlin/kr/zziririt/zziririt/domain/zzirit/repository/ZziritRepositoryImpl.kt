package kr.zziririt.zziririt.domain.zzirit.repository

import kr.zziririt.zziririt.domain.zzirit.model.ZziritEntity
import kr.zziririt.zziririt.domain.zzirit.model.ZziritEntityType
import kr.zziririt.zziririt.infra.jpa.zzirit.ZziritJpaRepository
import org.springframework.stereotype.Repository

@Repository
class ZziritRepositoryImpl(
    private val zziritJpaRepository: ZziritJpaRepository
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
}
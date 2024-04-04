package kr.zziririt.zziririt.domain.zzirit.repository

import kr.zziririt.zziririt.api.zzirit.dto.response.ZziritCountResponse
import kr.zziririt.zziririt.domain.zzirit.model.ZziritEntity
import kr.zziririt.zziririt.infra.querydsl.zzirit.dto.ZziritDto
import java.time.LocalDateTime

interface ZziritRepository {
    fun save(entity: ZziritEntity): ZziritEntity
    fun findZziritByMemberIdAndPostIdOrNull(socialMemberId: Long, postId: Long): ZziritEntity?
    fun findZziritByMemberIdAndCommentIdOrNull(socialMemberId: Long, commentId: Long): ZziritEntity?
    fun countZziritByPostId(postId: Long): Long
    fun countZziritByCommentId(commentId: Long): Long
    fun findZziritRankInPosts(range: LocalDateTime): List<ZziritDto>
    fun updateRank(zziritRank: List<ZziritDto>)
    fun findZziritPostRankingInRedis() : List<ZziritCountResponse>?
}
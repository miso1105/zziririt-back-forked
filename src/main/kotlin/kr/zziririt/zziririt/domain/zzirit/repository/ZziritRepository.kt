package kr.zziririt.zziririt.domain.zzirit.repository

import kr.zziririt.zziririt.domain.zzirit.model.ZziritEntity

interface ZziritRepository {
    fun save(entity: ZziritEntity): ZziritEntity
    fun findZziritByMemberIdAndPostIdOrNull(socialMemberId: Long, postId: Long): ZziritEntity?
    fun findZziritByMemberIdAndCommentIdOrNull(socialMemberId: Long, commentId: Long): ZziritEntity?
    fun countZziritByPostId(postId: Long): Long
    fun countZziritByCommentId(commentId: Long): Long
}
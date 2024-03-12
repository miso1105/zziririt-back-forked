package kr.zziririt.zziririt.domain.post.repository

import kr.zziririt.zziririt.api.post.dto.PostSearchCondition
import kr.zziririt.zziririt.domain.post.model.PostEntity
import kr.zziririt.zziririt.infra.querydsl.post.dto.PostRowDto
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable

interface PostRepository {
    fun findByIdOrNull(id: Long): PostEntity?
    fun save(entity: PostEntity): PostEntity
    fun delete(entity: PostEntity)
    fun findAll(): List<PostEntity>
    fun findAllById(idList: List<Long>): List<PostEntity>
    fun searchByWhere(condition: PostSearchCondition, pageable: Pageable): PageImpl<PostRowDto>
    fun saveSearchPostCacheKeyByPostId(postId: Long, searchPostCacheKey: String)
    fun clearAllSearchPostCacheRelatedToPostId(postId: Long)
}
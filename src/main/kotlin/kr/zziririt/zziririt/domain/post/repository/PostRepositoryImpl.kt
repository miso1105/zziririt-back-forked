package kr.zziririt.zziririt.domain.post.repository

import kr.zziririt.zziririt.api.post.dto.PostSearchCondition
import kr.zziririt.zziririt.domain.post.model.PostEntity
import kr.zziririt.zziririt.infra.jpa.post.PostJpaRepository
import kr.zziririt.zziririt.infra.querydsl.post.PostQueryDslRepositoryImpl
import kr.zziririt.zziririt.infra.querydsl.post.dto.PostRowDto
import kr.zziririt.zziririt.infra.redis.post.PostSearchCacheRedisRepository
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

@Repository
class PostRepositoryImpl(
    private val postJpaRepository: PostJpaRepository,
    private val postQueryDslRepositoryImpl: PostQueryDslRepositoryImpl,
    private val postSearchCacheRedisRepository: PostSearchCacheRedisRepository
) : PostRepository {
    override fun findByIdOrNull(id: Long): PostEntity? = postJpaRepository.findByIdOrNull(id)
    override fun save(entity: PostEntity): PostEntity = postJpaRepository.save(entity)
    override fun delete(entity: PostEntity) = postJpaRepository.delete(entity)
    override fun findAll(): List<PostEntity> = postJpaRepository.findAll()
    override fun findAllById(idList: List<Long>): List<PostEntity> = postJpaRepository.findAllById(idList)
    override fun searchByWhere(condition: PostSearchCondition, pageable: Pageable): PageImpl<PostRowDto> =
        postQueryDslRepositoryImpl.searchByWhere(condition, pageable)
    override fun saveSearchPostCacheKeyByPostId(postId: Long, searchPostCacheKey: String) =
        postSearchCacheRedisRepository.saveSearchPostCacheKeyByPostId(postId, searchPostCacheKey)
    override fun clearAllSearchPostCacheRelatedToPostId(postId: Long) =
        postSearchCacheRedisRepository.clearAllSearchPostCacheRelatedToPostId(postId)
}
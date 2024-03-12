package kr.zziririt.zziririt.api.post.service

import kr.zziririt.zziririt.api.post.dto.PostSearchCondition
import kr.zziririt.zziririt.api.post.dto.request.CreatePostRequest
import kr.zziririt.zziririt.api.post.dto.request.UpdatePostRequest
import kr.zziririt.zziririt.api.post.dto.response.PostResponse
import kr.zziririt.zziririt.domain.board.repository.BoardRepository
import kr.zziririt.zziririt.domain.member.repository.SocialMemberRepository
import kr.zziririt.zziririt.domain.post.repository.PostRepository
import kr.zziririt.zziririt.global.exception.ErrorCode
import kr.zziririt.zziririt.global.exception.ModelNotFoundException
import kr.zziririt.zziririt.global.exception.RestApiException
import kr.zziririt.zziririt.infra.querydsl.post.dto.PostRowDto
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PostService(
    private val postRepository: PostRepository,
    private val socialMemberRepository: SocialMemberRepository,
    private val boardRepository: BoardRepository
) {
    @Transactional
    fun createPost(
        boardId: Long,
        req: CreatePostRequest,
        authenticatedMemberId: Long
    ): PostResponse {
        val findSocialMember = socialMemberRepository.findByIdOrNull(authenticatedMemberId)
            ?: throw ModelNotFoundException(ErrorCode.MODEL_NOT_FOUND)
        val findBoard =
            boardRepository.findByIdOrNull(boardId) ?: throw ModelNotFoundException(ErrorCode.MODEL_NOT_FOUND)

        return req
            .member(findSocialMember)
            .board(findBoard)
            .toEntity().let {
                postRepository.save(it)
            }.let {
                PostResponse.from(it)
            }
    }

    @Transactional(readOnly = true)
    fun getPost(postId: Long): PostResponse {
        val findPost = postRepository.findByIdOrNull(postId)
            ?: throw ModelNotFoundException(ErrorCode.MODEL_NOT_FOUND)

        findPost.incrementHit()

        return PostResponse.from(findPost)
    }

    @Transactional(readOnly = true)
    fun getPosts(condition: PostSearchCondition): PageImpl<PostRowDto> {
        return postRepository.searchByWhere(
            condition,
            PageRequest.of(condition.page.toInt() - 1, condition.size.toInt())
        )
    }

    @Transactional
    @Cacheable(
        cacheNames = ["POST_SEARCH"],
        keyGenerator = "PostSearchKeyGenerator",
        cacheManager = "caffeineCacheManager"
    )
    fun getPostsWithCache(condition: PostSearchCondition): PageImpl<PostRowDto> {
        return postRepository.searchByWhere(
            condition,
            PageRequest.of(condition.page.toInt() - 1, condition.size.toInt())
        ).onEach { postRepository.saveSearchPostCacheKeyByPostId(it.postId, condition.makePostSearchCacheKey()) }
    }

    @Transactional
    @Cacheable(
        cacheNames = ["POST_SEARCH"],
        keyGenerator = "PostSearchKeyGenerator",
        cacheManager = "redisCacheManager"
    )
    fun getPostsWithRedisCache(condition: PostSearchCondition): PageImpl<PostRowDto> {
        val searchByWhere = postRepository.searchByWhere(
            condition,
            PageRequest.of(condition.page.toInt() - 1, condition.size.toInt())
        )
        searchByWhere.forEach { postRepository.saveSearchPostCacheKeyByPostId(it.postId, condition.makePostSearchCacheKey()) }

        return searchByWhere
    }

    @Transactional
    fun updatePost(
        postId: Long,
        updatePostRequest: UpdatePostRequest,
        authenticatedMemberId: Long
    ) {
        val findPost = postRepository.findByIdOrNull(postId) ?: throw ModelNotFoundException(ErrorCode.MODEL_NOT_FOUND)

        check(authenticatedMemberId == findPost.socialMember.id) { throw RestApiException(ErrorCode.UNAUTHORIZED) }

        postRepository.clearAllSearchPostCacheRelatedToPostId(postId)

        findPost.update(
            title = updatePostRequest.title,
            content = updatePostRequest.content
        )
        check(updatePostRequest.privateStatus == findPost.privateStatus) { findPost.togglePrivateStatus() }
    }

    @Transactional
    fun deletePost(
        postId: Long,
        authenticatedMemberId: Long
    ) {
        val findPost = postRepository.findByIdOrNull(postId) ?: throw ModelNotFoundException(ErrorCode.MODEL_NOT_FOUND)

        check(authenticatedMemberId == findPost.socialMember.id) { throw RestApiException(ErrorCode.UNAUTHORIZED) }

        postRepository.clearAllSearchPostCacheRelatedToPostId(postId)

        postRepository.delete(findPost)
    }
}
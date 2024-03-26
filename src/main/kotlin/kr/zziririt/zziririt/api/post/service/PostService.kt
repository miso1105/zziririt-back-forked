package kr.zziririt.zziririt.api.post.service

import kr.zziririt.zziririt.api.post.dto.PostSearchCondition
import kr.zziririt.zziririt.api.post.dto.request.CreatePostRequest
import kr.zziririt.zziririt.api.post.dto.request.UpdatePostRequest
import kr.zziririt.zziririt.api.post.dto.response.PostResponse
import kr.zziririt.zziririt.domain.board.repository.BoardRepository
import kr.zziririt.zziririt.domain.member.model.MemberRole
import kr.zziririt.zziririt.domain.member.repository.SocialMemberRepository
import kr.zziririt.zziririt.domain.post.repository.PostRepository
import kr.zziririt.zziririt.global.exception.ErrorCode
import kr.zziririt.zziririt.global.exception.ModelNotFoundException
import kr.zziririt.zziririt.global.exception.RestApiException
import kr.zziririt.zziririt.infra.querydsl.post.dto.PostRowDto
import kr.zziririt.zziririt.infra.security.UserPrincipal
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
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
                PostResponse.of(it, true, true)
            }
    }

    fun getAllPosts(pageable: Pageable): PageImpl<PostRowDto> {
        return postRepository.findAll(pageable)
    }

    @Transactional
    fun getPost(userPrincipal: UserPrincipal?, postId: Long): PostResponse {
        val findPost = postRepository.findByIdOrNull(postId)
            ?: throw ModelNotFoundException(ErrorCode.MODEL_NOT_FOUND)

        var permissionToUpdateStatus = false
        var permissionToDeleteStatus = false
        if (userPrincipal != null) {
            val findSocialMember = socialMemberRepository.findByIdOrNull(userPrincipal.memberId)
                ?: throw ModelNotFoundException(ErrorCode.MODEL_NOT_FOUND)

            if (findPost.socialMember.id == userPrincipal.memberId) {
                permissionToUpdateStatus = true
                permissionToDeleteStatus = true
            } else if (findSocialMember.memberRole == MemberRole.ADMIN ||
                userPrincipal.memberId == findPost.board.id
            ) {
                permissionToDeleteStatus = true
            }
        }

        findPost.incrementHit()

        return PostResponse.of(findPost, permissionToUpdateStatus, permissionToDeleteStatus)
    }

    @Transactional(readOnly = true)
    fun getPosts(userPrincipal: UserPrincipal?, boardId: Long, pageable: Pageable): PageImpl<PostRowDto> {
        val findBoard =
            boardRepository.findByIdOrNull(boardId) ?: throw ModelNotFoundException(ErrorCode.MODEL_NOT_FOUND)

        return postRepository.findAllByBoardId(boardId, pageable).map {
            if (userPrincipal != null &&
                (it.memberId == userPrincipal.memberId ||
                        userPrincipal.authorities.any { grandAuthority -> grandAuthority.authority == "ROLE_ADMIN" } ||
                        userPrincipal.memberId == findBoard.socialMember.id
                        )
            ) {
                it.permissionToRead = true
            }
            it as PostRowDto
        }.let {
            PageImpl(it.toList(), it.pageable, it.totalElements)
        }
    }

    @Transactional(readOnly = true)
    fun getPostsBySearch(condition: PostSearchCondition): PageImpl<PostRowDto> {
        return postRepository.searchByWhere(
            condition,
            PageRequest.of(condition.page.toInt(), condition.size.toInt())
        )
    }

    @Transactional
    @Cacheable(
        cacheNames = ["POST_SEARCH"],
        keyGenerator = "PostSearchKeyGenerator",
        cacheManager = "caffeineCacheManager"
    )
    fun getPostsBySearchWithCache(condition: PostSearchCondition): PageImpl<PostRowDto> {
        return postRepository.searchByWhere(
            condition,
            PageRequest.of(condition.page.toInt(), condition.size.toInt())
        ).onEach { postRepository.saveSearchPostCacheKeyByPostId(it.postId, condition.makePostSearchCacheKey()) }
    }

    @Transactional
    @Cacheable(
        cacheNames = ["POST_SEARCH"],
        keyGenerator = "PostSearchKeyGenerator",
        cacheManager = "redisCacheManager"
    )
    fun getPostsBySearchWithRedisCache(
        condition: PostSearchCondition
    ): PageImpl<PostRowDto> {
        val searchByWhere = postRepository.searchByWhere(
            condition,
            PageRequest.of(condition.page.toInt(), condition.size.toInt())
        )
        searchByWhere.forEach {
            postRepository.saveSearchPostCacheKeyByPostId(
                it.postId,
                condition.makePostSearchCacheKey()
            )
        }

        return searchByWhere
    }

    @Transactional
    fun updatePost(
        postId: Long,
        updatePostRequest: UpdatePostRequest,
        userPrincipal: UserPrincipal
    ) {
        val findPost = postRepository.findByIdOrNull(postId) ?: throw ModelNotFoundException(ErrorCode.MODEL_NOT_FOUND)

        check(userPrincipal.memberId == findPost.socialMember.id ||
                userPrincipal.authorities.any { grandAuthority -> grandAuthority.authority == "ROLE_ADMIN" }
        ) { throw RestApiException(ErrorCode.UNAUTHORIZED) }

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
        userPrincipal: UserPrincipal
    ) {
        val findPost = postRepository.findByIdOrNull(postId) ?: throw ModelNotFoundException(ErrorCode.MODEL_NOT_FOUND)

        check(userPrincipal.memberId == findPost.socialMember.id ||
                userPrincipal.authorities.any { grandAuthority -> grandAuthority.authority == "ROLE_ADMIN" } ||
                userPrincipal.memberId == findPost.board.socialMember.id
        ) { throw RestApiException(ErrorCode.UNAUTHORIZED) }

        postRepository.clearAllSearchPostCacheRelatedToPostId(postId)

        postRepository.delete(findPost)
    }
}
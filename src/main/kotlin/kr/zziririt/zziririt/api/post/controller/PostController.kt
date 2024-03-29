package kr.zziririt.zziririt.api.post.controller

import jakarta.validation.Valid
import kr.zziririt.zziririt.api.dto.CommonResponse
import kr.zziririt.zziririt.api.post.dto.PostSearchCondition
import kr.zziririt.zziririt.api.post.dto.request.CreatePostRequest
import kr.zziririt.zziririt.api.post.dto.request.UpdatePostRequest
import kr.zziririt.zziririt.api.post.service.PostService
import kr.zziririt.zziririt.global.responseEntity
import kr.zziririt.zziririt.infra.security.UserPrincipal
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api")
class PostController(
    private val postService: PostService
) {
    @PostMapping("/v1/boards/{boardId}/posts")
    fun createPost(
        @PathVariable boardId: Long,
        @Valid @RequestBody createPostRequest: CreatePostRequest,
        @AuthenticationPrincipal userPrincipal: UserPrincipal
    ) = responseEntity(HttpStatus.OK) { postService.createPost(boardId, createPostRequest, userPrincipal.memberId) }

    @GetMapping("/v1/boards/allposts")
    fun getAllPosts(
        @PageableDefault(
            size = 30,
            sort = [],
            page = 0,
        ) pageable: Pageable
    ) = responseEntity(HttpStatus.OK) { postService.getAllPosts(pageable) }

    @GetMapping("/v1/boards/{boardId}/posts/{postId}")
    fun getPost(
        @PathVariable postId: Long,
        @AuthenticationPrincipal(expression = "#this == 'anonymousUser' ? null : #this") userPrincipal: UserPrincipal?
    ) = responseEntity(HttpStatus.OK) { postService.getPost(userPrincipal, postId) }

    @GetMapping("/v1/boards/{boardId}/posts")
    fun getPosts(
        @PathVariable boardId: Long,
        @PageableDefault(
            size = 30,
            sort = [],
            page = 0,
        ) pageable: Pageable,
        @AuthenticationPrincipal(expression = "#this == 'anonymousUser' ? null : #this") userPrincipal: UserPrincipal?
    ) = responseEntity(HttpStatus.OK) { postService.getPosts(userPrincipal, boardId, pageable) }

    @GetMapping("/v1/boards/{boardId}/posts/search")
    fun getPostsBySearch(
        @PathVariable boardId: Long,
        @Valid condition: PostSearchCondition
    ) = responseEntity(HttpStatus.OK) { postService.getPostsBySearch(boardId, condition) }

    @GetMapping("/v2/boards/{boardId}/posts/search")
    fun getPostsWithCacheBySearch(
        @PathVariable boardId: Long,
        @Valid condition: PostSearchCondition
    ) = responseEntity(HttpStatus.OK) { postService.getPostsBySearchWithCache(boardId, condition) }

    @GetMapping("/v3/boards/{boardId}/posts/search")
    fun getPostsWithRedisCacheBySearch(
        @PathVariable boardId: Long,
        @Valid condition: PostSearchCondition
    ) = responseEntity(HttpStatus.OK) { postService.getPostsBySearchWithRedisCache(boardId, condition) }

    @PutMapping("/v1/boards/{boardId}/posts/{postId}")
    fun updatePost(
        @PathVariable postId: Long,
        @Valid @RequestBody updatePostRequest: UpdatePostRequest,
        @AuthenticationPrincipal userPrincipal: UserPrincipal
    ): ResponseEntity<CommonResponse<Nothing>> {
        postService.updatePost(postId, updatePostRequest, userPrincipal)
        return responseEntity(HttpStatus.OK)
    }

    @DeleteMapping("/v1/boards/{boardId}/posts/{postId}")
    fun deletePost(
        @PathVariable postId: Long,
        @AuthenticationPrincipal userPrincipal: UserPrincipal
    ): ResponseEntity<CommonResponse<Nothing>> {
        postService.deletePost(postId, userPrincipal)
        return responseEntity(HttpStatus.NO_CONTENT)
    }

    @PostMapping("/v1/boards/{boardId}/posts/{postId}/zzirit")
    fun toggleZzirit(
        @PathVariable postId: Long,
        @AuthenticationPrincipal userPrincipal: UserPrincipal
    ) = responseEntity(HttpStatus.OK) { postService.toggleZzirit(postId, userPrincipal) }

    @GetMapping("/v1/boards/{boardId}/posts/{postId}/zzirit")
    fun countZziritByPostId(
        @PathVariable postId: Long
    ) = responseEntity(HttpStatus.OK) { postService.countZziritByPostId(postId) }
}
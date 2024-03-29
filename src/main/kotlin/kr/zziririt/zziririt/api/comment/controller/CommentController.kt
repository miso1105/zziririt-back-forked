package kr.zziririt.zziririt.api.comment.controller

import jakarta.validation.Valid
import kr.zziririt.zziririt.api.comment.dto.CommentRequest
import kr.zziririt.zziririt.api.comment.service.CommentService
import kr.zziririt.zziririt.api.dto.CommonResponse
import kr.zziririt.zziririt.global.responseEntity
import kr.zziririt.zziririt.infra.security.UserPrincipal
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/boards/{boardId}/posts/{postId}/comments")
class CommentController(
    private val commentService: CommentService
) {
    @PostMapping
    fun createComment(
        @PathVariable postId: Long,
        @Valid @RequestBody commentRequest: CommentRequest,
        @AuthenticationPrincipal userPrincipal: UserPrincipal
    ): ResponseEntity<CommonResponse<Nothing>> {
        commentService.createComment(postId, commentRequest, userPrincipal)
        return responseEntity(HttpStatus.CREATED)
    }

    @PatchMapping("/{commentId}")
    fun updateComment(
        @PathVariable postId: Long,
        @PathVariable commentId: Long,
        @Valid @RequestBody commentRequest: CommentRequest,
        @AuthenticationPrincipal userPrincipal: UserPrincipal
    ): ResponseEntity<CommonResponse<Nothing>> {
        commentService.updateComment(postId, commentId, commentRequest, userPrincipal)
        return responseEntity(HttpStatus.OK)
    }

    @DeleteMapping("/{commentId}")
    fun deleteComment(
        @PathVariable commentId: Long,
        @AuthenticationPrincipal userPrincipal: UserPrincipal
    ): ResponseEntity<CommonResponse<Nothing>> {
        commentService.deleteComment(commentId, userPrincipal)
        return responseEntity(HttpStatus.NO_CONTENT)
    }

    @PostMapping("/{commentId}/zzirit")
    fun toggleZzirit(
        @PathVariable commentId: Long,
        @AuthenticationPrincipal userPrincipal: UserPrincipal
    ) = responseEntity(HttpStatus.OK) { commentService.toggleZzirit(commentId, userPrincipal) }

    @GetMapping("/{commentId}/zzirit")
    fun countZziritByPostId(
        @PathVariable commentId: Long
    ) = responseEntity(HttpStatus.OK) { commentService.countZziritByCommentId(commentId) }
}
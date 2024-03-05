package kr.zziririt.zziririt.api.comment.controller

import jakarta.validation.Valid
import kr.zziririt.zziririt.api.comment.dto.CommentDto
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
        @Valid @RequestBody commentDto: CommentDto,
        @AuthenticationPrincipal userPrincipal: UserPrincipal
    ): ResponseEntity<CommonResponse<Nothing>> {
        commentService.createComment(postId, commentDto, userPrincipal)
        return responseEntity(HttpStatus.CREATED)
    }

    @PatchMapping("/{commentId}")
    fun updateComment(
        @PathVariable postId: Long,
        @PathVariable commentId: Long,
        @Valid @RequestBody commentDto: CommentDto,
        @AuthenticationPrincipal userPrincipal: UserPrincipal
    ): ResponseEntity<CommonResponse<Nothing>> {
        commentService.updateComment(postId, commentId, commentDto, userPrincipal)
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

    @PostMapping("/{commentId}")
    fun incrementZzrit(
        @PathVariable commentId: Long
    ): ResponseEntity<CommonResponse<Nothing>> {
        commentService.incrementZzirit(commentId)
        return responseEntity(HttpStatus.CREATED)
    }
}
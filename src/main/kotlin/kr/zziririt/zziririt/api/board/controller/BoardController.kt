package kr.zziririt.zziririt.api.board.controller

import jakarta.validation.Valid
import kr.zziririt.zziririt.api.board.dto.BoardDto
import kr.zziririt.zziririt.api.board.dto.FavoriteBoardDto
import kr.zziririt.zziririt.api.board.service.BoardService
import kr.zziririt.zziririt.api.dto.CommonResponse
import kr.zziririt.zziririt.global.responseEntity
import kr.zziririt.zziririt.infra.security.UserPrincipal
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/boards")
class BoardController(
    private val boardService: BoardService
) {
    @PostMapping
    fun createBoard(
        @Valid
        @RequestBody boardDto: BoardDto,
        @AuthenticationPrincipal userPrincipal: UserPrincipal
    ): ResponseEntity<CommonResponse<Nothing>> {
        boardService.createBoard(boardDto, userPrincipal)
        return responseEntity(HttpStatus.CREATED)
    }

    @PostMapping("/{boardId}")
    fun createStreamerBoard(
        @Valid
        @PathVariable boardId: Long,
        @RequestBody boardDto: BoardDto,
        @AuthenticationPrincipal userPrincipal: UserPrincipal
    ): ResponseEntity<CommonResponse<Nothing>> {
        boardService.createChildBoard(boardId, boardDto, userPrincipal)
        return responseEntity(HttpStatus.CREATED)
    }

    @PatchMapping("/{boardId}")
    fun updateBoard(
        @Valid
        @PathVariable boardId: Long,
        @RequestBody boardDto: BoardDto,
        @AuthenticationPrincipal userPrincipal: UserPrincipal
    ): ResponseEntity<CommonResponse<Nothing>> {
        boardService.updateBoard(boardId, boardDto, userPrincipal)
        return responseEntity(HttpStatus.OK)
    }

    @DeleteMapping("/{boardId}")
    fun deleteBoard(
        @PathVariable boardId: Long,
        @AuthenticationPrincipal userPrincipal: UserPrincipal
    ): ResponseEntity<CommonResponse<Nothing>> {
        boardService.deleteBoard(boardId, userPrincipal)
        return responseEntity(HttpStatus.NO_CONTENT)
    }

    @PostMapping("/favorites")
    fun createFavoriteBoard(
        @Valid
        @RequestBody favoriteBoardDto: FavoriteBoardDto,
        @AuthenticationPrincipal userPrincipal: UserPrincipal
    ): ResponseEntity<CommonResponse<Nothing>> {
        boardService.createFavoriteBoard(favoriteBoardDto, userPrincipal)
        return responseEntity(HttpStatus.CREATED)
    }

    @DeleteMapping("/favorites/{favoriteBoardId}")
    fun deleteFavoriteBoard(
        @PathVariable favoriteBoardId: Long,
        @AuthenticationPrincipal userPrincipal: UserPrincipal
    ): ResponseEntity<CommonResponse<Nothing>> {
        boardService.deleteFavoriteBoard(favoriteBoardId, userPrincipal)
        return responseEntity(HttpStatus.NO_CONTENT)
    }
}
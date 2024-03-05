package kr.zziririt.zziririt.api.board.service

import kr.zziririt.zziririt.api.board.dto.BoardDto
import kr.zziririt.zziririt.api.board.dto.FavoriteBoardDto
import kr.zziririt.zziririt.domain.board.repository.BoardRepository
import kr.zziririt.zziririt.domain.board.repository.FavoriteBoardRepository
import kr.zziririt.zziririt.domain.member.repository.SocialMemberRepository
import kr.zziririt.zziririt.global.exception.ErrorCode
import kr.zziririt.zziririt.global.exception.ModelNotFoundException
import kr.zziririt.zziririt.infra.security.UserPrincipal
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class BoardService(
    private val socialMemberRepository: SocialMemberRepository,
    private val boardRepository: BoardRepository,
    private val favoriteBoardRepository: FavoriteBoardRepository
) {
    fun createBoard(boardDto: BoardDto, userPrincipal: UserPrincipal) {
        val findSocialMember = socialMemberRepository.findByIdOrNull(userPrincipal.memberId)
            ?: throw ModelNotFoundException(ErrorCode.MODEL_NOT_FOUND)

        boardRepository.save(boardDto.to(socialMemberEntity = findSocialMember, parent = null))
    }

    fun createChildBoard(boardId: Long, boardDto: BoardDto, userPrincipal: UserPrincipal) {
        val findSocialMember = socialMemberRepository.findByIdOrNull(userPrincipal.memberId)
            ?: throw ModelNotFoundException(ErrorCode.MODEL_NOT_FOUND)
        val findParentBoard =
            boardRepository.findByIdOrNull(boardId) ?: throw ModelNotFoundException(ErrorCode.MODEL_NOT_FOUND)

        boardRepository.save(boardDto.to(socialMemberEntity = findSocialMember, parent = findParentBoard))
    }

    @Transactional
    fun updateBoard(boardId: Long, boardDto: BoardDto, userPrincipal: UserPrincipal) {
        socialMemberRepository.findByIdOrNull(userPrincipal.memberId)
            ?: throw ModelNotFoundException(ErrorCode.MODEL_NOT_FOUND)
        val findBoard =
            boardRepository.findByIdOrNull(boardId) ?: throw ModelNotFoundException(ErrorCode.MODEL_NOT_FOUND)

        findBoard.update(boardName = boardDto.boardName)

    }

    fun deleteBoard(boardId: Long, userPrincipal: UserPrincipal) {
        socialMemberRepository.findByIdOrNull(userPrincipal.memberId)
            ?: throw ModelNotFoundException(ErrorCode.MODEL_NOT_FOUND)
        val findBoard =
            boardRepository.findByIdOrNull(boardId) ?: throw ModelNotFoundException(ErrorCode.MODEL_NOT_FOUND)

        boardRepository.delete(findBoard)
    }

    fun createFavoriteBoard(favoriteBoardDto: FavoriteBoardDto, userPrincipal: UserPrincipal) {
        val findSocialMember = socialMemberRepository.findByIdOrNull(userPrincipal.memberId)
            ?: throw ModelNotFoundException(ErrorCode.MODEL_NOT_FOUND)
        val findBoard =
            boardRepository.findByIdOrNull(favoriteBoardDto.favoriteBoardId.toLong()) ?: throw ModelNotFoundException(
                ErrorCode.MODEL_NOT_FOUND
            )

        favoriteBoardRepository.save(
            favoriteBoardDto.to(
                socialMemberEntity = findSocialMember,
                boardEntity = findBoard
            )
        )
    }

    fun deleteFavoriteBoard(favoriteBoardId: Long, userPrincipal: UserPrincipal) {
        socialMemberRepository.findByIdOrNull(userPrincipal.memberId)
            ?: throw ModelNotFoundException(ErrorCode.MODEL_NOT_FOUND)
        val findFavBoard = favoriteBoardRepository.findByIdOrNull(favoriteBoardId) ?: throw ModelNotFoundException(
            ErrorCode.MODEL_NOT_FOUND
        )

        favoriteBoardRepository.delete(findFavBoard)
    }

}

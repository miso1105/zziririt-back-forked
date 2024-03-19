package kr.zziririt.zziririt.api.board.service

import kr.zziririt.zziririt.api.board.dto.BoardDto
import kr.zziririt.zziririt.api.board.dto.StreamerFormDto
import kr.zziririt.zziririt.api.board.dto.SubscribeBoardDto
import kr.zziririt.zziririt.domain.board.repository.BoardRepository
import kr.zziririt.zziririt.domain.board.repository.StreamerFormRepository
import kr.zziririt.zziririt.domain.member.repository.SocialMemberRepository
import kr.zziririt.zziririt.global.exception.ErrorCode
import kr.zziririt.zziririt.global.exception.ModelNotFoundException
import kr.zziririt.zziririt.global.exception.RestApiException
import kr.zziririt.zziririt.infra.aws.S3Service
import kr.zziririt.zziririt.infra.querydsl.board.BoardRowDto
import kr.zziririt.zziririt.infra.querydsl.board.StreamerBoardRowDto
import kr.zziririt.zziririt.infra.security.UserPrincipal
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile


@Service
class BoardService(
    private val socialMemberRepository: SocialMemberRepository,
    private val boardRepository: BoardRepository,
    private val streamerFormRepository: StreamerFormRepository,
    private val s3Service: S3Service,
) {
    @Transactional
    fun createStreamerForm(
        multipartFile: List<MultipartFile>,
        streamerFormDto: StreamerFormDto,
        userPrincipal: UserPrincipal
    ) {
        val findSocialMember = socialMemberRepository.findByIdOrNull(userPrincipal.memberId)
            ?: throw ModelNotFoundException(ErrorCode.MODEL_NOT_FOUND)

        check(!boardRepository.existsBoardEntityByBoardName(streamerFormDto.applyBoardName)) {
            throw RestApiException(ErrorCode.DUPLICATE_MODEL_NAME)
        }

        val saveForm = streamerFormRepository.save(streamerFormDto.to(socialMemberEntity = findSocialMember))
        val imageUrl = s3Service.uploadFiles(dir = "streamer_image", files = multipartFile)
        saveForm.uploadImage(imageUrl.toString())

    }

    @Transactional
    fun updateStreamerForm(
        streamerFormDto: StreamerFormDto,
        multipartFile: List<MultipartFile>,
        userPrincipal: UserPrincipal
    ) {
        socialMemberRepository.findByIdOrNull(userPrincipal.memberId)
            ?: throw ModelNotFoundException(ErrorCode.MODEL_NOT_FOUND)

        val findStreamerForm = streamerFormRepository.findByIdOrNull(userPrincipal.memberId)
            ?: throw ModelNotFoundException(ErrorCode.MODEL_NOT_FOUND)
        findStreamerForm.update(applyUrl = streamerFormDto.applyUrl, applyBoardName = streamerFormDto.applyBoardName)

        val imageUrl = s3Service.uploadFiles(dir = "update_streamer_image", files = multipartFile)
        findStreamerForm.uploadImage(imageUrl.toString())
    }

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

    @Transactional
    fun createSubscribeBoard(subscribeBoardDto: SubscribeBoardDto, userPrincipal: UserPrincipal) {
        val findSocialMember = socialMemberRepository.findByIdOrNull(userPrincipal.memberId)
            ?: throw ModelNotFoundException(ErrorCode.MODEL_NOT_FOUND)
        val findBoard =
            boardRepository.findByIdOrNull(subscribeBoardDto.subscribeBoardId) ?: throw ModelNotFoundException(
                ErrorCode.MODEL_NOT_FOUND
            )

        findSocialMember.subscribeBoardsList.add(findBoard.id!!)

        socialMemberRepository.save(findSocialMember)
    }

    @Transactional
    fun unSubscribeBoard(subscribeBoardDto: SubscribeBoardDto, userPrincipal: UserPrincipal) {
        val findSocialMember = socialMemberRepository.findByIdOrNull(userPrincipal.memberId)
            ?: throw ModelNotFoundException(ErrorCode.MODEL_NOT_FOUND)
        val findBoard =
            boardRepository.findByIdOrNull(subscribeBoardDto.subscribeBoardId) ?: throw ModelNotFoundException(
                ErrorCode.MODEL_NOT_FOUND
            )

        findSocialMember.subscribeBoardsList.remove(findBoard.id)

        socialMemberRepository.save(findSocialMember)
    }

    fun getBoards(pageable: Pageable): Page<BoardRowDto> {
        return boardRepository.findByPageable(pageable)
    }

    fun getActiveStatusBoards(pageable: Pageable): Page<BoardRowDto> {
        return boardRepository.findActiveStatusBoards(pageable)
    }

    fun getStreamers(pageable: Pageable): Page<StreamerBoardRowDto> {
        return boardRepository.findStreamersByPageable(pageable)
    }

    @Transactional
    @Scheduled(cron = "0 0 0 * * *")
    fun boardScheduler() {
        val inactiveBoardIdList = boardRepository.findInactiveBoardStatus()

        boardRepository.updateBoardStatusToInactive(inactiveBoardIdList)
    }
}
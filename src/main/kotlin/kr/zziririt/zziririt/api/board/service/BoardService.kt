package kr.zziririt.zziririt.api.board.service

import kr.zziririt.zziririt.api.board.dto.request.*
import kr.zziririt.zziririt.api.board.dto.response.BoardResponse
import kr.zziririt.zziririt.api.board.dto.response.BoardUrlSearchResponse
import kr.zziririt.zziririt.api.board.dto.response.CategoryResponse
import kr.zziririt.zziririt.domain.board.model.CategoryEntity
import kr.zziririt.zziririt.domain.board.repository.BoardRepository
import kr.zziririt.zziririt.domain.board.repository.StreamerBoardApplicationRepository
import kr.zziririt.zziririt.domain.member.model.MemberRole
import kr.zziririt.zziririt.domain.member.repository.SocialMemberRepository
import kr.zziririt.zziririt.global.exception.ErrorCode
import kr.zziririt.zziririt.global.exception.ModelNotFoundException
import kr.zziririt.zziririt.global.exception.RestApiException
import kr.zziririt.zziririt.infra.aws.s3.S3Service
import kr.zziririt.zziririt.infra.querydsl.board.BoardRowDto
import kr.zziririt.zziririt.infra.querydsl.board.StreamerBoardRowDto
import kr.zziririt.zziririt.infra.security.UserPrincipal
import org.springframework.data.repository.findByIdOrNull
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile


@Service
class BoardService(
    private val socialMemberRepository: SocialMemberRepository,
    private val boardRepository: BoardRepository,
    private val streamerBoardApplicationRepository: StreamerBoardApplicationRepository,
    private val s3Service: S3Service,
) {
    @Transactional
    fun createStreamerBoardApplication(
        streamerBoardApplicationRequest: StreamerBoardApplicationRequest,
        multipartFile: List<MultipartFile>,
        userPrincipal: UserPrincipal
    ) {
        val findSocialMember = socialMemberRepository.findByIdOrNull(userPrincipal.memberId)
            ?: throw ModelNotFoundException(ErrorCode.MODEL_NOT_FOUND)

        if (findSocialMember.memberRole == MemberRole.STREAMER) {
            throw RestApiException(ErrorCode.DUPLICATE_ROLE)
        }

        check(!boardRepository.existsBoardEntityByBoardName(streamerBoardApplicationRequest.applyBoardName)) {
            throw RestApiException(ErrorCode.DUPLICATE_MODEL_NAME)
        }

        check(!boardRepository.existsBoardEntityByBoardUrl(streamerBoardApplicationRequest.applyUrl)) {
            throw RestApiException(ErrorCode.DUPLICATE_MODEL_NAME)
        }

        val imageUrl = s3Service.uploadFiles(dir = "streamer_image", files = multipartFile)
        imageUrl.forEach {
            val streamerBoardApplication = streamerBoardApplicationRequest.to(socialMemberEntity = findSocialMember)
            streamerBoardApplication.uploadImage(it)
            streamerBoardApplicationRepository.save(streamerBoardApplication)
        }
    }

    @Transactional
    fun updateStreamerBoardApplication(
        streamerBoardApplicationId: Long,
        streamerBoardApplicationRequest: StreamerBoardApplicationRequest,
        multipartFile: List<MultipartFile>,
        userPrincipal: UserPrincipal
    ) {
        val findMember = socialMemberRepository.findByIdOrNull(userPrincipal.memberId)
            ?: throw ModelNotFoundException(ErrorCode.MODEL_NOT_FOUND)

        check(!boardRepository.existsBoardEntityByBoardName(streamerBoardApplicationRequest.applyBoardName)) {
            throw RestApiException(ErrorCode.DUPLICATE_MODEL_NAME)
        }

        check(!boardRepository.existsBoardEntityByBoardUrl(streamerBoardApplicationRequest.applyUrl)) {
            throw RestApiException(ErrorCode.DUPLICATE_MODEL_NAME)
        }

        val findStreamerApplication = streamerBoardApplicationRepository.findByIdOrNull(streamerBoardApplicationId)
            ?: throw ModelNotFoundException(ErrorCode.MODEL_NOT_FOUND)

        findStreamerApplication.update(
            applyUrl = streamerBoardApplicationRequest.applyUrl,
            applyBoardName = streamerBoardApplicationRequest.applyBoardName
        )

        val imageUrl = s3Service.uploadFiles(dir = "update_streamer_image", files = multipartFile)
        imageUrl.forEach {
            val streamerBoardApplication = streamerBoardApplicationRequest.to(socialMemberEntity = findMember)
            streamerBoardApplication.uploadImage(it)
        }
    }

    @Transactional
    fun createBoard(boardRequest: BoardRequest, userPrincipal: UserPrincipal) {
        val findMember = socialMemberRepository.findByIdOrNull(userPrincipal.memberId)
            ?: throw RestApiException(ErrorCode.MODEL_NOT_FOUND)

        boardRepository.save(boardRequest.to(socialMemberEntity = findMember))
    }

    @Transactional
    fun updateBoard(boardId: Long, boardRequest: BoardRequest, userPrincipal: UserPrincipal) {
        val findMember = socialMemberRepository.findByIdOrNull(userPrincipal.memberId) ?: throw RestApiException(
            ErrorCode.MODEL_NOT_FOUND
        )

        val findBoard =
            boardRepository.findByIdOrNull(boardId) ?: throw ModelNotFoundException(ErrorCode.MODEL_NOT_FOUND)

        findBoard.update(boardName = boardRequest.boardName, socialMember = findMember)
    }

    fun deleteBoard(boardId: Long, userPrincipal: UserPrincipal) {
        val findBoard =
            boardRepository.findByIdOrNull(boardId) ?: throw ModelNotFoundException(ErrorCode.MODEL_NOT_FOUND)

        boardRepository.delete(findBoard)
    }

    @Transactional
    fun createSubscribeBoard(subscribeBoardRequest: SubscribeBoardRequest, userPrincipal: UserPrincipal) {
        val findSocialMember = socialMemberRepository.findByIdOrNull(userPrincipal.memberId)
            ?: throw ModelNotFoundException(ErrorCode.MODEL_NOT_FOUND)
        val findBoard =
            boardRepository.findByIdOrNull(subscribeBoardRequest.subscribeBoardId) ?: throw ModelNotFoundException(
                ErrorCode.MODEL_NOT_FOUND
            )

        findSocialMember.subscribeBoardsList.add(findBoard.id!!)

        socialMemberRepository.save(findSocialMember)
    }

    @Transactional
    fun unSubscribeBoard(subscribeBoardRequest: SubscribeBoardRequest, userPrincipal: UserPrincipal) {
        val findSocialMember = socialMemberRepository.findByIdOrNull(userPrincipal.memberId)
            ?: throw ModelNotFoundException(ErrorCode.MODEL_NOT_FOUND)
        val findBoard =
            boardRepository.findByIdOrNull(subscribeBoardRequest.subscribeBoardId) ?: throw ModelNotFoundException(
                ErrorCode.MODEL_NOT_FOUND
            )

        findSocialMember.subscribeBoardsList.remove(findBoard.id)

        socialMemberRepository.save(findSocialMember)
    }

    fun getBoards(): List<BoardRowDto> {
        return boardRepository.findBoards()
    }

    fun getActiveStatusBoards(): List<BoardRowDto> {
        return boardRepository.findActiveStatusBoards()
    }

    fun getStreamers(): List<StreamerBoardRowDto> {
        return boardRepository.findStreamers()
    }

    @Transactional
    @Scheduled(cron = "0 0 0 * * *")
    fun boardScheduler() {
        val inactiveBoardIdList = boardRepository.findInactiveBoardStatus()

        boardRepository.updateBoardStatusToInactive(inactiveBoardIdList)
    }

    @Transactional
    fun createStreamerBoard(streamerBoardRequest: StreamerBoardRequest) {
        val boardOwner = socialMemberRepository.findByIdOrNull(streamerBoardRequest.boardOwnerId)
            ?: throw RestApiException(ErrorCode.MODEL_NOT_FOUND)

        boardOwner.toStreamer()

        val application = streamerBoardApplicationRepository.findByIdOrNull(streamerBoardRequest.streamerApplicationId)
            ?: throw ModelNotFoundException(ErrorCode.MODEL_NOT_FOUND)

        application.updateApplicationStatus(streamerBoardRequest.streamerBoardApplicationStatus)

        val board = boardRepository.save(streamerBoardRequest.to(boardOwner))
        board.categories.add(CategoryEntity(categoryName = "공지사항"))
        board.categories.add(CategoryEntity(categoryName = "잡담 게시판"))
    }

    fun getBoardById(boardId: Long): BoardResponse {
        val findBoard = boardRepository.findByIdOrNull(boardId) ?: throw RestApiException(ErrorCode.MODEL_NOT_FOUND)
        return BoardResponse.from(boardEntity = findBoard)
    }

    @Transactional
    fun addCategoryToBoard(boardId: Long, request: CreateCategoryRequest) {

        val board = boardRepository.findByIdOrNull(boardId)
            ?: throw ModelNotFoundException(ErrorCode.MODEL_NOT_FOUND)
        board.categories.add(CategoryEntity(request.categoryName))
    }

    @Transactional
    fun getBoardByUrl(boardUrl: String): BoardUrlSearchResponse {
        val board = boardRepository.findByBoardUrl(boardUrl)
        return BoardUrlSearchResponse.from(board)
    }

    @Transactional
    fun getCategoriesByBoardId(boardId: Long): List<CategoryResponse> {
        val board = boardRepository.findByIdOrNull(boardId) ?: throw RestApiException(ErrorCode.MODEL_NOT_FOUND)
        val categories = board.categories
        return categories.map { CategoryResponse.from(it) }
    }
}
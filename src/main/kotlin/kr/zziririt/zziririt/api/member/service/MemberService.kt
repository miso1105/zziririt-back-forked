package kr.zziririt.zziririt.api.member.service

import jakarta.transaction.Transactional
import kr.zziririt.zziririt.api.member.dto.request.AdjustRoleRequest
import kr.zziririt.zziririt.api.member.dto.request.SetBoardManagerRequest
import kr.zziririt.zziririt.api.member.dto.response.GetMemberResponse
import kr.zziririt.zziririt.domain.member.model.MemberRole
import kr.zziririt.zziririt.domain.member.repository.LoginHistoryRepository
import kr.zziririt.zziririt.domain.member.repository.SocialMemberRepository
import kr.zziririt.zziririt.global.exception.ErrorCode
import kr.zziririt.zziririt.global.exception.ModelNotFoundException
import kr.zziririt.zziririt.global.exception.RestApiException
import kr.zziririt.zziririt.infra.security.UserPrincipal
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Service
class MemberService(
    private val memberRepository: SocialMemberRepository,
    private val loginHistoryRepository: LoginHistoryRepository
) {

    fun getMember(userPrincipal: UserPrincipal): GetMemberResponse {
        val memberCheck = memberRepository.findByIdOrNull(userPrincipal.memberId) ?: throw ModelNotFoundException(
            ErrorCode.MODEL_NOT_FOUND
        )
        val loginCheck = loginHistoryRepository.findTopByMemberIdOrderByCreatedAtDesc(memberCheck)

        return GetMemberResponse.from(memberCheck, loginCheck)
    }

    @Transactional
    fun adjustRole(memberId: Long, request: AdjustRoleRequest, userPrincipal: UserPrincipal) {
        val memberCheck =
            memberRepository.findByIdOrNull(memberId) ?: throw ModelNotFoundException(ErrorCode.MODEL_NOT_FOUND)

        check(memberCheck.memberRole != request.memberRole) {
            throw RestApiException(ErrorCode.DUPLICATE_ROLE)
        }

        memberCheck.memberRole = request.memberRole
    }

    @Transactional
    fun delegateBoardManager(request: SetBoardManagerRequest, userPrincipal: UserPrincipal) {
        val boardManagerCheck =
            memberRepository.findByIdOrNull(request.memberId) ?: throw ModelNotFoundException(ErrorCode.MODEL_NOT_FOUND)

        check(boardManagerCheck.memberRole != MemberRole.BOARD_MANAGER) {
            throw RestApiException(ErrorCode.DUPLICATE_ROLE)
        }

        boardManagerCheck.toBoardManager()
    }

    @Transactional
    fun dismissBoardManager(request: SetBoardManagerRequest, userPrincipal: UserPrincipal) {
        val boardManagerCheck =
            memberRepository.findByIdOrNull(request.memberId) ?: throw ModelNotFoundException(ErrorCode.MODEL_NOT_FOUND)

        check(boardManagerCheck.memberRole == MemberRole.BOARD_MANAGER) {
            throw RestApiException(ErrorCode.DUPLICATE_ROLE)
        }

        boardManagerCheck.toViewer()
    }


    @Scheduled(cron = "0 0 3 1/1 * ?")
    @Transactional
    fun updateMemberStatusToSleeper() {
        val sleeperCandidates = memberRepository.findSleeperCandidatesMemberId()

        if(sleeperCandidates.isNotEmpty()) {
            memberRepository.bulkUpdateMemberStatusToSleeper(sleeperCandidates)
        }
    }

    @Transactional
    fun getSubscribeByMember(userPrincipal: UserPrincipal): List<Long> {
        val memberCheck = memberRepository.findByIdOrNull(userPrincipal.memberId) ?: throw ModelNotFoundException(ErrorCode.MODEL_NOT_FOUND)

        return memberCheck.subscribeBoardsList.map { it }
    }

}
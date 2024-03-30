package kr.zziririt.zziririt.api.member.service

import jakarta.transaction.Transactional
import kr.zziririt.zziririt.api.member.dto.request.AdjustRoleRequest
import kr.zziririt.zziririt.api.member.dto.request.ChangeMemberPointRequest
import kr.zziririt.zziririt.api.member.dto.request.SelectMyIconRequest
import kr.zziririt.zziririt.api.member.dto.request.SetBoardManagerRequest
import kr.zziririt.zziririt.api.member.dto.response.GetMyInfoResponse
import kr.zziririt.zziririt.api.point.service.PointHistoryService
import kr.zziririt.zziririt.domain.member.model.ChangeDivision
import kr.zziririt.zziririt.domain.member.model.MemberRole
import kr.zziririt.zziririt.domain.member.model.MemberStatus
import kr.zziririt.zziririt.domain.member.repository.LoginHistoryRepository
import kr.zziririt.zziririt.domain.member.repository.MemberIconRepository
import kr.zziririt.zziririt.domain.member.repository.SocialMemberRepository
import kr.zziririt.zziririt.domain.report.repository.BannedHistoryRepository
import kr.zziririt.zziririt.global.exception.ErrorCode
import kr.zziririt.zziririt.global.exception.ModelNotFoundException
import kr.zziririt.zziririt.global.exception.RestApiException
import kr.zziririt.zziririt.infra.security.UserPrincipal
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class MemberService(
    private val memberRepository: SocialMemberRepository,
    private val memberIconRepository: MemberIconRepository,
    private val loginHistoryRepository: LoginHistoryRepository,
    private val bannedHistoryRepository: BannedHistoryRepository,
    private val pointHistoryService: PointHistoryService
) {

    fun getMember(userPrincipal: UserPrincipal): GetMyInfoResponse {
        val memberCheck = memberRepository.findByIdOrNull(userPrincipal.memberId) ?: throw ModelNotFoundException(
            ErrorCode.MODEL_NOT_FOUND
        )
        val loginCheck = loginHistoryRepository.findTopByMemberIdOrderByCreatedAtDesc(memberCheck)

        return GetMyInfoResponse.from(memberCheck, loginCheck)
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

    @Transactional
    fun selectMyIcon(userPrincipal: UserPrincipal, request: SelectMyIconRequest) {
        val memberCheck = memberRepository.findByIdOrNull(userPrincipal.memberId)
            ?: throw ModelNotFoundException(ErrorCode.MODEL_NOT_FOUND)

        val iconCheck = memberIconRepository.existsByMemberIdAndIconId(memberCheck.id!!, request.iconId)

        check(iconCheck) {
            throw RestApiException(ErrorCode.NOT_HAVE_ICON)
        }

        memberCheck.changeDefaultIcon(request.iconId)
    }

    @Transactional
    @Scheduled(cron = "0 0/30 * 1/1 * ?")
    fun updateMemberStatusAfterBanPeriod() {
        val currentDate = LocalDateTime.now()
        val expiredBans = bannedHistoryRepository.findByBannedEndDateBeforeAndBannedMemberIdMemberStatus(currentDate, MemberStatus.BANNED)

        expiredBans.forEach {
            val newStatusMember = it.bannedMemberId
            newStatusMember.memberStatus = MemberStatus.NORMAL
            memberRepository.save(newStatusMember)
        }
    }

    @Transactional
    fun changeMemberPoint(request: ChangeMemberPointRequest) {
        val memberCheck = memberRepository.findByIdOrNull(request.memberId)
            ?: throw ModelNotFoundException(ErrorCode.MODEL_NOT_FOUND)

        when (request.pointChangeType) {
            ChangeDivision.PAYMENT -> {
                memberCheck.pointPlus(request.value)
                pointHistoryService.regPointHistory(request.value, "운영자 지급", memberCheck)
            }
            ChangeDivision.WITHDRAW -> {
                memberCheck.pointMinus(request.value)
                pointHistoryService.regPointHistory(request.value, "운영자 차감", memberCheck)
            }
        }
    }

}
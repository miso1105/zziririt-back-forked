package kr.zziririt.zziririt.api.member.service

import kr.zziririt.zziririt.api.member.dto.request.AdjustRoleRequest
import kr.zziririt.zziririt.api.member.dto.request.SetBoardManagerRequest
import kr.zziririt.zziririt.api.member.dto.response.GetMemberResponse
import kr.zziririt.zziririt.domain.member.model.MemberRole
import kr.zziririt.zziririt.domain.member.repository.SocialMemberRepository
import kr.zziririt.zziririt.global.exception.ErrorCode
import kr.zziririt.zziririt.global.exception.ModelNotFoundException
import kr.zziririt.zziririt.infra.security.UserPrincipal
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class MemberService(
    private val memberRepository: SocialMemberRepository
) {

    fun getMember(userPrincipal: UserPrincipal): GetMemberResponse {
        val memberCheck = memberRepository.findByIdOrNull(userPrincipal.memberId) ?: throw ModelNotFoundException(ErrorCode.MODEL_NOT_FOUND)

        return GetMemberResponse.from(memberCheck)
    }

    fun adjustRole(memberId: Long, request: AdjustRoleRequest, userPrincipal: UserPrincipal) {
        val adminCheck = memberRepository.findByIdOrNull(userPrincipal.memberId) ?: throw ModelNotFoundException(ErrorCode.MODEL_NOT_FOUND)
        val memberCheck = memberRepository.findByIdOrNull(memberId) ?: throw ModelNotFoundException(ErrorCode.MODEL_NOT_FOUND)

        check(adminCheck.memberRole == MemberRole.ADMIN) {
            throw ModelNotFoundException(ErrorCode.UNAUTHORIZED)
        }

        check(memberCheck.memberRole != request.memberRole) {
            throw ModelNotFoundException(ErrorCode.VALIDATION)
        }

        memberCheck.memberRole = request.memberRole

        memberRepository.save(memberCheck)
    }

    fun delegateBoardManager(request: SetBoardManagerRequest, userPrincipal: UserPrincipal) {
        val streamerCheck = memberRepository.findByIdOrNull(userPrincipal.memberId) ?: throw ModelNotFoundException(ErrorCode.MODEL_NOT_FOUND)
        val boardManagerCheck =
            memberRepository.findByIdOrNull(request.memberId) ?: throw ModelNotFoundException(ErrorCode.MODEL_NOT_FOUND)

        check(streamerCheck.memberRole == MemberRole.STREAMER || streamerCheck.memberRole == MemberRole.ADMIN) {
            throw ModelNotFoundException(ErrorCode.UNAUTHORIZED)
        }

        check(boardManagerCheck.memberRole != MemberRole.BOARD_MANAGER) {
            throw ModelNotFoundException(ErrorCode.VALIDATION)
        }

        boardManagerCheck.memberRole = MemberRole.BOARD_MANAGER

        memberRepository.save(boardManagerCheck)
    }

    fun dismissBoardManager(request: SetBoardManagerRequest, userPrincipal: UserPrincipal) {
        val streamerCheck = memberRepository.findByIdOrNull(userPrincipal.memberId) ?: throw ModelNotFoundException(ErrorCode.MODEL_NOT_FOUND)
        val boardManagerCheck =
            memberRepository.findByIdOrNull(request.memberId) ?: throw ModelNotFoundException(ErrorCode.MODEL_NOT_FOUND)

        check(streamerCheck.memberRole == MemberRole.STREAMER || streamerCheck.memberRole == MemberRole.ADMIN) {
            throw ModelNotFoundException(ErrorCode.UNAUTHORIZED)
        }

        check(boardManagerCheck.memberRole == MemberRole.BOARD_MANAGER) {
            throw ModelNotFoundException(ErrorCode.VALIDATION)
        }

        boardManagerCheck.memberRole = MemberRole.VIEWER

        memberRepository.save(boardManagerCheck)
    }

}
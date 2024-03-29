package kr.zziririt.zziririt.api.icon.service

import jakarta.transaction.Transactional
import kr.zziririt.zziririt.api.icon.dto.request.AddIconRequest
import kr.zziririt.zziririt.api.icon.dto.request.GiveIconToMemberRequest
import kr.zziririt.zziririt.domain.icon.model.IconBackOfficeDivision
import kr.zziririt.zziririt.domain.icon.repository.IconRepository
import kr.zziririt.zziririt.domain.member.model.MemberIconEntity
import kr.zziririt.zziririt.domain.member.repository.MemberIconRepository
import kr.zziririt.zziririt.domain.member.repository.SocialMemberRepository
import kr.zziririt.zziririt.global.exception.ErrorCode
import kr.zziririt.zziririt.global.exception.ModelNotFoundException
import kr.zziririt.zziririt.global.exception.RestApiException
import kr.zziririt.zziririt.infra.aws.S3Service
import kr.zziririt.zziririt.infra.querydsl.member.dto.GetMyIconsDto
import kr.zziririt.zziririt.infra.security.UserPrincipal
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
class IconService (
    private val memberRepository: SocialMemberRepository,
    private val iconRepository: IconRepository,
    private val s3Service: S3Service,
    private val memberIconRepository: MemberIconRepository,
) {

    @Transactional
    fun addIcon(requestObj: AddIconRequest, file: List<MultipartFile>, userPrincipal: UserPrincipal) {
        memberRepository.findByIdOrNull(userPrincipal.memberId)
            ?: throw ModelNotFoundException(ErrorCode.MODEL_NOT_FOUND)
        val iconUrl = s3Service.uploadFiles("icon", file)

        iconUrl.forEach {
            val newIcon = requestObj.toEntity(it)
            iconRepository.save(newIcon)
        }
    }

    fun deleteIcon(iconId: Long) {
        val iconCheck = iconRepository.findByIdOrNull(iconId) ?: throw ModelNotFoundException(ErrorCode.MODEL_NOT_FOUND)

        iconRepository.delete(iconCheck)
    }

    fun getMyIcons(pageable: Pageable, userPrincipal: UserPrincipal): PageImpl<GetMyIconsDto> {
        memberRepository.findByIdOrNull(userPrincipal.memberId)
            ?: throw ModelNotFoundException(ErrorCode.MODEL_NOT_FOUND)

        return memberIconRepository.getMyIcons(pageable)
    }

    @Transactional
    fun giveIconToMember(request: GiveIconToMemberRequest) {
        val memberCheck = memberRepository.findByIdOrNull(request.memberId)
            ?: throw ModelNotFoundException(ErrorCode.MODEL_NOT_FOUND)

        val iconCheck = iconRepository.findByIdOrNull(request.iconId)
            ?: throw ModelNotFoundException(ErrorCode.MODEL_NOT_FOUND)

        when (request.division) {
            IconBackOfficeDivision.PAYMENT -> {
                val alreadyHasIcon = memberIconRepository.existsByMemberIdAndIconId(memberCheck.id!!, iconCheck.id)
                check(!alreadyHasIcon) { throw RestApiException(ErrorCode.ALREADY_HAVE_ICON) }

                memberIconRepository.save(MemberIconEntity(memberCheck, iconCheck))
            }

            IconBackOfficeDivision.RECOVERY -> {
                val iconEntity = memberIconRepository.findByMemberIdAndIconId(memberCheck.id!!, iconCheck.id)
                    ?: throw RestApiException(ErrorCode.NOT_HAVE_ICON)

                memberIconRepository.delete(iconEntity)
            }
        }
    }
}
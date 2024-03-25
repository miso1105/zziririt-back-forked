package kr.zziririt.zziririt.api.icon.service

import jakarta.transaction.Transactional
import kr.zziririt.zziririt.api.icon.dto.request.AddIconRequest
import kr.zziririt.zziririt.domain.icon.repository.IconRepository
import kr.zziririt.zziririt.domain.member.repository.MemberIconRepository
import kr.zziririt.zziririt.domain.member.repository.SocialMemberRepository
import kr.zziririt.zziririt.global.exception.ErrorCode
import kr.zziririt.zziririt.global.exception.ModelNotFoundException
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
    fun addIcon(request: AddIconRequest, file: List<MultipartFile>, userPrincipal: UserPrincipal){
        memberRepository.findByIdOrNull(userPrincipal.memberId)
            ?: throw ModelNotFoundException(ErrorCode.MODEL_NOT_FOUND)
        val iconUrl = s3Service.uploadFiles("icon",file)

        iconUrl.forEach {
            val newIcon = request.toEntity(it)
            iconRepository.save(newIcon)
        }
    }

    fun deleteIcon(iconId: Long) {
        val iconCheck = iconRepository.findByIdOrNull(iconId) ?: throw ModelNotFoundException(ErrorCode.MODEL_NOT_FOUND)

        iconRepository.delete(iconCheck)
    }

    fun getMyIcons(pageable: Pageable, userPrincipal: UserPrincipal) : PageImpl<GetMyIconsDto> {
        memberRepository.findByIdOrNull(userPrincipal.memberId)
            ?: throw ModelNotFoundException(ErrorCode.MODEL_NOT_FOUND)

        return memberIconRepository.getMyIcons(pageable)
    }
}
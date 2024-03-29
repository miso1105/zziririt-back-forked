package kr.zziririt.zziririt.infra.oauth2.service

import kr.zziririt.zziririt.domain.member.model.OAuth2Provider
import kr.zziririt.zziririt.domain.member.model.SocialMemberEntity
import kr.zziririt.zziririt.domain.member.repository.SocialMemberRepository
import kr.zziririt.zziririt.infra.mailsender.MailService
import kr.zziririt.zziririt.infra.oauth2.dto.OAuth2MemberInfo
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Service
class SocialMemberService (
    private val socialMemberRepository: SocialMemberRepository,
    private val mailService: MailService
){
    fun registerIfAbsent(memberInfo: OAuth2MemberInfo): SocialMemberEntity {
        val provider = OAuth2Provider.valueOf(memberInfo.provider)

        if (!socialMemberRepository.existsByProviderAndProviderId(provider, memberInfo.providerId)) {
            val socialMember = SocialMemberEntity.ofNaver(memberInfo.providerId, memberInfo.nickname, memberInfo.email, memberInfo.provider, memberInfo.memberRole, memberInfo.memberStatus)
            val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val formattedDate = LocalDateTime.now().format(dateFormatter)
            mailService.sendMailToNewbie(recipient = socialMember.email, nickname = socialMember.nickname, createdAt = formattedDate)
            return socialMemberRepository.save(socialMember)
        } else {
            return socialMemberRepository.findByProviderAndProviderId(provider, memberInfo.providerId)
        }
    }
}
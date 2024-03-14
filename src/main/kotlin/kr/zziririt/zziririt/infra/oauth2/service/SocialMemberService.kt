package kr.zziririt.zziririt.infra.oauth2.service

import kr.zziririt.zziririt.domain.member.model.OAuth2Provider
import kr.zziririt.zziririt.domain.member.model.SocialMemberEntity
import kr.zziririt.zziririt.domain.member.repository.SocialMemberRepository
import kr.zziririt.zziririt.infra.oauth2.dto.OAuth2MemberInfo
import org.springframework.stereotype.Service

@Service
class SocialMemberService (
    private val socialMemberRepository: SocialMemberRepository
){
    fun registerIfAbsent(memberInfo: OAuth2MemberInfo): SocialMemberEntity {
        val provider = OAuth2Provider.valueOf(memberInfo.provider)
        return if (!socialMemberRepository.existsByProviderAndProviderId(provider, memberInfo.providerId)) {
            val socialMember = SocialMemberEntity.ofNaver(memberInfo.providerId, memberInfo.nickname, memberInfo.email, memberInfo.provider, memberInfo.memberRole, memberInfo.memberStatus)
            socialMemberRepository.save(socialMember)
        } else {
            socialMemberRepository.findByProviderAndProviderId(provider, memberInfo.providerId)
        }
    }
}
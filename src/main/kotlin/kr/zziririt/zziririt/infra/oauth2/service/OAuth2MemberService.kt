package kr.zziririt.zziririt.infra.oauth2.service

import kr.zziririt.zziririt.infra.oauth2.dto.OAuth2MemberInfo
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service

@Service
class OAuth2MemberService (
    private val socialMemberService: SocialMemberService
) : DefaultOAuth2UserService(){

    override fun loadUser(userRequest: OAuth2UserRequest): OAuth2User {
        val originUser = super.loadUser(userRequest)
        val provider = userRequest.clientRegistration.clientName // "NAVER"
        return OAuth2MemberInfo.of(provider, userRequest, originUser)
            .also { socialMemberService.registerIfAbsent(it) }
    }

}
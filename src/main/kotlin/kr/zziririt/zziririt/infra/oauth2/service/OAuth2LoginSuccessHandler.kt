package kr.zziririt.zziririt.infra.oauth2.service

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import kr.zziririt.zziririt.domain.member.repository.SocialMemberRepository
import kr.zziririt.zziririt.infra.oauth2.dto.OAuth2MemberInfo
import kr.zziririt.zziririt.infra.security.jwt.JwtProvider
import org.springframework.http.MediaType
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.stereotype.Component

@Component
class OAuth2LoginSuccessHandler(
    private val memberRepository: SocialMemberRepository,
    private val jwtProvider: JwtProvider
) : AuthenticationSuccessHandler {

    override fun onAuthenticationSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication
    ) {
        val memberInfo = authentication.principal as OAuth2MemberInfo
        val member = memberRepository.findByEmail(memberInfo.email)
        val accessToken = jwtProvider.generateAccessToken(
            subject = member?.id!!,
            email = memberInfo.email,
            providerId = memberInfo.providerId,
            role = member.memberRole.toString(),
            status = memberInfo.memberStatus.toString()
        )
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.writer.write(accessToken)
    }
}
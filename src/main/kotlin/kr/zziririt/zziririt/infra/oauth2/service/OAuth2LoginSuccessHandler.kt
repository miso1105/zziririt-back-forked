package kr.zziririt.zziririt.infra.oauth2.service

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import kr.zziririt.zziririt.domain.member.model.LoginHistoryEntity
import kr.zziririt.zziririt.domain.member.repository.LoginHistoryRepository
import kr.zziririt.zziririt.domain.member.repository.SocialMemberRepository
import kr.zziririt.zziririt.infra.oauth2.dto.OAuth2MemberInfo
import kr.zziririt.zziririt.infra.security.UserPrincipal
import kr.zziririt.zziririt.infra.security.jwt.JwtAuthenticationToken
import kr.zziririt.zziririt.infra.security.jwt.JwtProvider
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component

@Component
class OAuth2LoginSuccessHandler(
    @Value("\${auth.redirect-url}") private val redirectUrl: String,
    private val memberRepository: SocialMemberRepository,
    private val loginHistoryRepository: LoginHistoryRepository,
    private val jwtProvider: JwtProvider
) : AuthenticationSuccessHandler {

    override fun onAuthenticationSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication
    ) {
        val memberInfo = authentication.principal as OAuth2MemberInfo
        val member = memberRepository.findByEmail(memberInfo.email)

        val ip = request.remoteAddr
        val environment = request.getHeader("user-Agent")

        val accessToken = jwtProvider.generateAccessToken(
            subject = member?.id!!,
            email = memberInfo.email,
            providerId = memberInfo.providerId,
            role = member.memberRole.toString(),
            status = memberInfo.memberStatus.toString()
        )

        val principal = UserPrincipal(
            memberId = member.id!!,
            email = member.email,
            roles = setOf(member.memberRole.name),
            providerId = member.providerId
        )

        val newAuthentication = JwtAuthenticationToken(
            principal = principal,
            details = WebAuthenticationDetailsSource().buildDetails(request)
        )
        SecurityContextHolder.getContext().authentication = newAuthentication

        loginHistoryRepository.save(LoginHistoryEntity(member, ip, environment))

        response.addHeader("Authorization", "Bearer $accessToken")
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.status = HttpStatus.TEMPORARY_REDIRECT.value()
        response.setHeader("Location", "$redirectUrl?Authorization=Bearer $accessToken")
    }
}
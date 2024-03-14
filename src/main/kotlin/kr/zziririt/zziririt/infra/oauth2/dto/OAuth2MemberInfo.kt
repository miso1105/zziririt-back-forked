package kr.zziririt.zziririt.infra.oauth2.dto

import kr.zziririt.zziririt.domain.member.model.MemberRole
import kr.zziririt.zziririt.domain.member.model.MemberStatus
import kr.zziririt.zziririt.global.exception.ErrorCode
import kr.zziririt.zziririt.global.exception.ModelNotFoundException
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.core.user.OAuth2User

data class OAuth2MemberInfo(
    val providerId: String,
    val provider: String,
    val nickname: String,
    val email: String,
    val memberRole: MemberRole,
    val memberStatus: MemberStatus
) : OAuth2User {

    override fun getName(): String = providerId

    override fun getAttributes(): MutableMap<String, Any> = mutableMapOf()

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> = mutableListOf()

    companion object {
        fun of(provider: String, userRequest: OAuth2UserRequest, originUser: OAuth2User): OAuth2MemberInfo {
            return when (provider) {
                "NAVER", "naver" -> ofNaver(provider, userRequest, originUser)
                else -> throw ModelNotFoundException(ErrorCode.MODEL_NOT_FOUND)
            }
        }

        private fun ofNaver(
            provider: String,
            userRequest: OAuth2UserRequest,
            originUser: OAuth2User
        ): OAuth2MemberInfo {
            val profile = originUser.attributes["response"] as Map<*, *>
            val nickname = profile["nickname"] ?: "null"
            val providerId = profile["id"] ?: "null"
            val email = profile["email"] ?: "null"

            return OAuth2MemberInfo(
                providerId = providerId as String,
                provider = provider.uppercase(),
                nickname = nickname as String,
                email = email as String,
                memberRole = MemberRole.VIEWER,
                memberStatus = MemberStatus.NORMAL
            )
        }

    }

}
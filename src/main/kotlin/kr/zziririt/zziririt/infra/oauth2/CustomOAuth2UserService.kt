package kr.zziririt.zziririt.infra.oauth2

import jakarta.transaction.Transactional
import kr.zziririt.zziririt.domain.member.model.MemberRole
import kr.zziririt.zziririt.domain.member.model.OAuth2Provider
import kr.zziririt.zziririt.domain.member.model.SocialMemberEntity
import kr.zziririt.zziririt.domain.member.repository.SocialMemberRepository
import kr.zziririt.zziririt.infra.security.jwt.JwtDto
import kr.zziririt.zziririt.infra.security.jwt.JwtProvider
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.core.OAuth2AuthenticationException
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service

@Service
class CustomOAuth2UserService(
    private val socialMemberRepository: SocialMemberRepository,
    private val jwtProvider: JwtProvider
) : DefaultOAuth2UserService() {

    @Throws(OAuth2AuthenticationException::class)
    override fun loadUser(userRequest: OAuth2UserRequest) : OAuth2User {

        val loadUser = super.loadUser(userRequest)

        return loadUser
    }

    fun getAttribute(attribute: String, oAuth2User: OAuth2User) : String? {
        return (oAuth2User.attributes.get("response") as LinkedHashMap<String, String>).get(attribute)!!
    }

    @Transactional
    fun login(oAuth2User: OAuth2User) : JwtDto {
        val email = getAttribute("email", oAuth2User)!!
        val user = socialMemberRepository.findByEmail(email)
        if (user == null) {
            socialMemberRepository.save(SocialMemberEntity(
                email = email,
                name = getAttribute("name", oAuth2User)!!,
                nickname = getAttribute("nickname", oAuth2User)!!,
                profileImageUrl = getAttribute("profile_image", oAuth2User)!!,
                providerId = getAttribute("id", oAuth2User)!!,
                provider = OAuth2Provider.NAVER,
                birthday = getAttribute("birthday", oAuth2User)!!,
                age = getAttribute("age", oAuth2User)!!,
                birthyear = getAttribute("birthyear", oAuth2User)!!,
                gender = getAttribute("gender", oAuth2User)!!,
                mobile = getAttribute("mobile", oAuth2User)!!,
                memberRole = MemberRole.VIEWER
            ))
        }
        return jwtProvider.generateJwtDto(email)
    }

}
package kr.zziririt.zziririt.infra.oauth2

import kr.zziririt.zziririt.infra.security.jwt.JwtDto
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/oauth2")
class OAuthLoginController(
    private val authService: CustomOAuth2UserService
) {
    @GetMapping("/login")
    fun login(@AuthenticationPrincipal oAuth2User: OAuth2User): ResponseEntity<JwtDto> {
        return ResponseEntity.ok(authService.login(oAuth2User))
    }
}
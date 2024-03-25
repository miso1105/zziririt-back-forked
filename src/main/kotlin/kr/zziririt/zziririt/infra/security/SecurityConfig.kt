package kr.zziririt.zziririt.infra.security

import kr.zziririt.zziririt.infra.oauth2.service.OAuth2LoginSuccessHandler
import kr.zziririt.zziririt.infra.oauth2.service.OAuth2MemberService
import kr.zziririt.zziririt.infra.security.jwt.JwtAuthenticationFilter
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
class SecurityConfig(
    @Value("\${auth.front-host}") private val frontHost: String,
    private val jwtAuthenticationFilter: JwtAuthenticationFilter,
    private val oAuth2MemberService: OAuth2MemberService,
    private val oAuth2LoginSuccessHandler: OAuth2LoginSuccessHandler
) {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        return http
            .httpBasic { it.disable() }
            .formLogin { it.disable() }
            .csrf { it.disable() }
            .headers { it.frameOptions { options -> options.sameOrigin() } }
            .cors { it.configurationSource(corsConfigurationSource()) }
            .headers { headers -> headers.frameOptions { frameOptions -> frameOptions.sameOrigin() } }
            .sessionManagement {
                it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .oauth2Login { oauthConfig ->
                oauthConfig.authorizationEndpoint {
                    it.baseUri("/api/v1/oauth2/login")
                }.redirectionEndpoint {
                    it.baseUri("/api/v1/oauth2/callback/*")
                }.userInfoEndpoint {
                    it.userService(oAuth2MemberService)
                }.successHandler(oAuth2LoginSuccessHandler)
            }
            .addFilterAfter(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
            .build()
    }
    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration().apply {
            allowedOriginPatterns = listOf(frontHost)
            addAllowedMethod("*")
            addAllowedHeader("*")
            allowCredentials = true
            maxAge = 3600L
        }
        return UrlBasedCorsConfigurationSource().apply {
            registerCorsConfiguration("/**", configuration)
        }
    }
}
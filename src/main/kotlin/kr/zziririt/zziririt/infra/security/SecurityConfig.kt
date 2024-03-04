package kr.zziririt.zziririt.infra.security

import kr.zziririt.zziririt.infra.oauth2.CustomOAuth2UserService
import kr.zziririt.zziririt.infra.security.jwt.JwtAuthenticationFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
class SecurityConfig (
    private val jwtAuthenticationFilter: JwtAuthenticationFilter,
    private val customOAuth2UserService: CustomOAuth2UserService,
){
    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        return http
            .httpBasic { it.disable() }
            .formLogin { it.disable() }
            .csrf { it.disable() }
            .headers { headers -> headers.frameOptions { frameOptions -> frameOptions.sameOrigin() } }
            .sessionManagement {
                it.sessionCreationPolicy(SessionCreationPolicy.NEVER)
            }
            .oauth2Login {
                it.userInfoEndpoint { u -> u.userService (customOAuth2UserService)}
                it.defaultSuccessUrl("/api/v1/oauth2/login")
                it.failureUrl("/error")
            }
            .authorizeHttpRequests {
                it.requestMatchers(
                    "**",
                    "/swagger-ui/**",
                    "/v3/api-docs/**",
                )
                    .permitAll()
                    .anyRequest().authenticated()
            }
            .addFilterAfter(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
            .build()
    }
}
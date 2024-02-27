package kr.zziririt.zziririt.api

import kr.zziririt.zziririt.domain.member.repository.SocialMemberRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HealthCheckController(private val socialMemberRepository: SocialMemberRepository) {
    @GetMapping("/ping")
    fun ping() = "pong"
}
package kr.zziririt.zziririt.infra.aop

import kr.zziririt.zziririt.domain.member.model.MemberStatus
import kr.zziririt.zziririt.domain.member.repository.SocialMemberRepository
import kr.zziririt.zziririt.global.exception.ErrorCode
import kr.zziririt.zziririt.global.exception.ModelNotFoundException
import kr.zziririt.zziririt.infra.oauth2.dto.OAuth2MemberInfo
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component

@Component
@Aspect
class BanCheckAspect {

    @Autowired
    lateinit var memberRepository: SocialMemberRepository


    @Around("@annotation(kr.zziririt.zziririt.infra.aop.BanCheck)")
    fun banCheck(
        joinPoint: ProceedingJoinPoint,
    ): Any? {

        val authentication = SecurityContextHolder.getContext().authentication
        val userPrincipal = authentication.principal as OAuth2MemberInfo

        val member = memberRepository.findByEmail(userPrincipal.email)
            ?: throw ModelNotFoundException(ErrorCode.MODEL_NOT_FOUND)

        if (member.memberStatus == MemberStatus.BANNED) {
            throw IllegalStateException("BANNED MEMBER")
        }

        val result = joinPoint.proceed()

        return result
    }
}
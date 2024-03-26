package kr.zziririt.zziririt.global.config

import kr.zziririt.zziririt.infra.security.UserPrincipal
import org.springframework.data.domain.AuditorAware
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import java.util.*

@Service
class MemberAwareAudit : AuditorAware<Long> {

    override fun getCurrentAuditor(): Optional<Long> {
        val authentication: Authentication? = SecurityContextHolder.getContext().authentication
        if (authentication == null || (authentication.authorities.any { it.authority == "OAUTH2_USER" }) || authentication.principal == "anonymousUser") {
            return Optional.empty()
        }

        return Optional.of((authentication.principal as UserPrincipal).memberId)
    }

}
package kr.zziririt.zziririt.infra.security

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority

data class UserPrincipal(
    val memberId: Long,
    val email: String,
    val authorities: Collection<GrantedAuthority>,
    val providerId: String
) {
    constructor(memberId: Long, email: String, roles: Set<String>, providerId: String) : this(
        memberId,
        email,
        roles.map { SimpleGrantedAuthority("ROLE_$it") },
        providerId
    )
}